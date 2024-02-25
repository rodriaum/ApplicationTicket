package com.github.rodriaum.ticket.config;

import com.github.rodriaum.ticket.config.key.ConfigKey;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.OnlineStatus;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Config {

    private static final Map<String, Object> map = new HashMap<>();
    private static final String CONFIG_FILE_PATH = "config.yml";

    public static void init() {
        File file = new File(CONFIG_FILE_PATH);

        if (file.exists())
            System.out.println("Arquivo config.yml detectado, não será criado um novo.");
        else {
            load();
        }
    }

    public static String getToken() {
        return (String) map.get("token");
    }

    public static OnlineStatus getStatus() {
        return OnlineStatus.fromKey((String) map.get(ConfigKey.ONLINE_STATUS.getKey()));
    }

    public static void load() {
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
            System.out.println("Desligando o aplicativo para que você possa ajustar as configurações....");

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao criar o arquivo config.yml.\n" + e.getMessage());
        }
    }
}
