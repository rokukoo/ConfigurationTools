package cn.neptunex.configuration.driver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YamlConfigurationDriver implements ConfigurationDriver {

    private final File file;
    private FileConfiguration fileConfiguration;
    private final FileWatchTask watchTask;

    public YamlConfigurationDriver(File file, FileConfiguration fileConfiguration) {
        this.file = file;
        this.fileConfiguration = fileConfiguration;
        this.watchTask = new FileWatchTask();
        // TODO: 这是文件自动重新加载的一个范例代码, 慎用
//        this.watchTask.start();
    }

//    public YamlConfigurationDriver(Reader reader) {
//        this(YamlConfiguration.loadConfiguration(reader));
//    }

    public YamlConfigurationDriver(File file) {
        this(file, YamlConfiguration.loadConfiguration(file));
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
        fileConfiguration.save(new File(fileConfiguration.getCurrentPath()));
    }

    class FileWatchTask extends Thread{

        private ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5);

        @Override
        public void run() {
            WatchService service = null;
            try {
                service = FileSystems.getDefault().newWatchService();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String directory = file.getParent();
            try {
                Paths.get(directory).register(service, StandardWatchEventKinds.ENTRY_MODIFY);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                WatchKey key = null;
                try {
                    key = service.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (WatchEvent<?> event : Objects.requireNonNull(key).pollEvents()) {
                    if (event.context().toString().equals(file.getName())){
                        fileConfiguration = YamlConfiguration.loadConfiguration(file);
                    }
                }
                if (!key.reset()) break;
            }
        }

    }

}
