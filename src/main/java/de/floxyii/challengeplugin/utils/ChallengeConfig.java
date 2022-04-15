package de.floxyii.challengeplugin.utils;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ChallengeConfig {
    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public ChallengeConfig() {
        File dir = new File("./plugins/ChallengePlugin/");
        if(!dir.exists() && !dir.mkdirs()) {
            ChallengePlugin.getPlugin().getLogger().info("Config cannot be created!");
        }
        file = new File(dir, "config.yml");
        if(!file.exists()) {
            try {
                if(!file.createNewFile()) {
                    ChallengePlugin.getPlugin().getLogger().info("Config cannot be created!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public boolean contains(String path) {
        return yamlConfiguration.contains(path);
    }

    public void set(String path, Object value) {
        yamlConfiguration.set(path, value);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object get(String path) {
        if(!contains(path)) {
            return null;
        }
        return yamlConfiguration.get(path);
    }
}
