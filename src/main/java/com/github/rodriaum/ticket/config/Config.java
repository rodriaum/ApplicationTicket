package com.github.rodriaum.ticket.config;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.github.rodriaum.ticket.config.key.ConfigKey;
import lombok.AllArgsConstructor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Config {

    private static final Map<String, Object> map = new HashMap<>();
    private static final String CONFIG_FILE_PATH = "config.yml";

    public static void init() {
        File file = new File(CONFIG_FILE_PATH);

        if (file.exists()) {
            System.out.println("Arquivo config.yml detectado, não será criado um novo.");

            for (int i = 0; i < map.size(); i++)
                map.put(ConfigKey.values()[i].getKey(), ConfigKey.values()[i].getValue());

        } else {
            load();
        }
    }

    public static Object getValue(String key) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> keys = yaml.load(Files.newInputStream(Paths.get(CONFIG_FILE_PATH)));

            return keys.get(key);

        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao pegar um valor no arquivo config.yml.\n" + e.getMessage());
        }

        return null;
    }

    public static Object getValue(ConfigKey key) {
        return getValue(key.getKey());
    }

    public static String getStringValue(ConfigKey key) {
        return (String) getValue(key.getKey());
    }

    private static void load() {
        System.out.println("Criando arquivo config.yml...");

        for (int i = 0; i < ConfigKey.values().length; i++)
            map.put(ConfigKey.values()[i].getKey(), ConfigKey.values()[i].getValue());

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        try {
            FileWriter writer = new FileWriter(CONFIG_FILE_PATH);

            yaml.dump(map, writer);
            writer.close();

            System.out.println("Arquivo config.yml criado com sucesso.");
            System.out.println("Desligando o bot para que você possa ajustar as configurações....");

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao criar o arquivo config.yml.\n" + e.getMessage());
        }
    }
}
