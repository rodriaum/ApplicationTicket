package com.github.rodriaum.ticket.config;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
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

    public static void init(JDA jda) {
        File file = new File(CONFIG_FILE_PATH);

        if (file.exists())
            System.out.println("Arquivo config.yml detectado, não será criado um novo.");
        else
            load(jda);
    }

    public static String getToken() {
        return (String) map.get("token");
    }

    private static void load(JDA jda) {
        System.out.println("Criando arquivo config.yml...");

        map.put("token", "");
        map.put("guild-id", "");
        map.put("channel.ticket", "");
        map.put("channel.log", "");
        map.put("category.ticket-received", "");

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        try {
            FileWriter writer = new FileWriter(CONFIG_FILE_PATH);
            yaml.dump(map, writer);
            writer.close();

            System.out.println("Arquivo config.yml criado com sucesso.");
            System.out.println("Desligando o aplicativo para que você possa ajustar as configurações....");

            jda.shutdown();

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao criar o arquivo config.yml.\n" + e.getMessage());
        }
    }
}
