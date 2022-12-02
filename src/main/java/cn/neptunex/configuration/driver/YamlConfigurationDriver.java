package cn.neptunex.configuration.driver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YamlConfigurationDriver implements ConfigurationDriver {

    private final File file;
    private FileConfiguration fileConfiguration;
    private final FileWatchTask watchTask;

    private static final ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5);

    // FIXME: 这里的最后一个参数到时候需要修改一下
    public YamlConfigurationDriver(File file, FileConfiguration fileConfiguration, boolean isAutoReload, Class<?> reloadCallback) {
        this.file = file;
        this.fileConfiguration = fileConfiguration;
        this.watchTask = new FileWatchTask();
        if (isAutoReload){
            cachedThreadPool.submit(watchTask);
            if (reloadCallback != void.class && reloadCallback != null){
                // TODO: 添加文件修改的回调
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

    public YamlConfigurationDriver(File file, boolean isReload) {
        this(file, YamlConfiguration.loadConfiguration(file), isReload, null);
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
                        Path filePath = (Path) event.context();
                        if (filePath.toString().equals(file.getName())){
                            fileConfiguration = YamlConfiguration.loadConfiguration(file);
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
