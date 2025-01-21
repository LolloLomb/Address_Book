import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private Properties properties;

    public PropertiesReader(String propertiesFilePath) throws IOException {
        properties = new Properties();
        File propertiesFile = new File(propertiesFilePath);

        if (!propertiesFile.exists()) {
            throw new IOException("No .properties file found at the specified path");
        }

        try (FileInputStream in = new FileInputStream(propertiesFile)) {
            properties.load(in);
        }
    }

    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password");
    }
}
