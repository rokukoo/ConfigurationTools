package cn.neptunex.configuration.driver;

import cn.neptunex.configuration.features.AutoConfiguration;
import cn.neptunex.configuration.features.ConfigurationReloadCallback;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YamlConfigurationDriver implements ConfigurationDriver {

    @Setter
    private AutoConfiguration instance;
    private final File file;
    private FileConfiguration fileConfiguration;
    private final FileWatchTask watchTask;
    private ConfigurationReloadCallback callback;

    private static final ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5);


    // FIXME: 这里的最后一个参数到时候需要修改一下
    public YamlConfigurationDriver(File file, FileConfiguration fileConfiguration, boolean isAutoReload, Class<? extends ConfigurationReloadCallback> reloadCallback) {
        this.file = file;
        this.fileConfiguration = fileConfiguration;
        this.watchTask = new FileWatchTask();
        if (isAutoReload){
            cachedThreadPool.submit(watchTask);
            if (reloadCallback != ConfigurationReloadCallback.class && reloadCallback != null){
                try {
                    Constructor<? extends ConfigurationReloadCallback> constructor = reloadCallback.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    this.callback = constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // TODO: 这里到时候可能要用网络IO来做试试看
//    public YamlConfigurationDriver(Reader reader) {
//        this(YamlConfiguration.loadConfiguration(reader));
//    }

    public YamlConfigurationDriver(File file) {
        this(file, YamlConfiguration.loadConfiguration(file), false, null);
    }

    public YamlConfigurationDriver(File file, boolean isAutoReload, Class<? extends ConfigurationReloadCallback> callback) {
        this(file, YamlConfiguration.loadConfiguration(file), isAutoReload, callback);
    }

    public YamlConfigurationDriver(File file, boolean isAutoReload) {
        this(file, YamlConfiguration.loadConfiguration(file), isAutoReload, null);
    }

    @Override
    public <T> T get(String path, Class<T> returnType) {
        if (returnType.equals(String.class)){
            fileConfiguration.getString(path);
        }
        return (T) fileConfiguration.get(path);
    }

    @Override
    public <T> void set(String path, T value) {
        fileConfiguration.set(path, value);
    }

    @Override
    public void save() throws IOException {
        fileConfiguration.save(file.getAbsoluteFile());
    }

    @Override
    public void reload() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (callback != null){
            callback.acceptReload(this.file, instance);
        }
    }

    class FileWatchTask extends Thread{

        @Override
        public void run() {
            String directory = file.getParent();
            try(WatchService service = FileSystems.getDefault().newWatchService()) {
                Paths.get(directory).register(Objects.requireNonNull(service), StandardWatchEventKinds.ENTRY_MODIFY);
                while (true){
                    WatchKey key = service.take();
                    for (WatchEvent<?> event : Objects.requireNonNull(key).pollEvents()) {
                        // Prevent receiving two separate ENTRY_MODIFY events: file modified
                        // and timestamp updated. Instead, receive one ENTRY_MODIFY event
                        // with two counts.
                        Thread.sleep( 50 );
                        Path filePath = (Path) event.context();
                        if (filePath.toString().equals(file.getName())){
                            reload();
                        }
                    }
                    if (!key.reset()) break;
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
