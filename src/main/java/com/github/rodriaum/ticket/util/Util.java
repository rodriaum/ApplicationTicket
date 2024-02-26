package com.github.rodriaum.ticket.util;

import com.github.rodriaum.ticket.config.Config;
import com.github.rodriaum.ticket.config.key.ConfigKey;
import com.github.rodriaum.ticket.util.embed.Embeds;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class Util {

    public static String formatName(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String formatToUtf8(String s) {
        return new String(s.getBytes(), StandardCharsets.UTF_8);
    }

    private static File saveMessagesAndEmbedsToFile(List<Message> messages, String fileName) {
        File file = new File(fileName);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (Message message : messages) {
                StringBuilder content = new StringBuilder();

                content.append(message.getAuthor().getName()).append(": ");
                content.append(message.getContentDisplay());

                List<MessageEmbed> embeds = message.getEmbeds();

                if (!embeds.isEmpty()) {
                    for (MessageEmbed embed : embeds) {
                        if (embed.getDescription() != null) {
                            content.append("\n------------------------------\nEmbed Description: ").append(Util.formatToUtf8(embed.getDescription().replace("```", "")));
                        }
                    }
                }

                writer.write(content.toString());
                writer.newLine();
            }

            writer.close();
            System.out.println(formatToUtf8("Mensagens do último ticket fechado salvas em " + file.getAbsolutePath()));

        } catch (IOException e) {
            System.err.println(formatToUtf8("Não foi possível criar o arquivo com as mensagens do último ticket.\n" + e.getMessage()));
        }

        return file;
    }


    public static void generateTicketLogFile(TextChannel channel, TextChannel logsChannel) {
        if (logsChannel != null) {
            File logsDir = new File("logs");

            if (!logsDir.exists())
                logsDir.mkdirs();

            logsChannel.sendFiles(
                    FileUpload.fromData(
                            saveMessagesAndEmbedsToFile(
                                    channel.getIterableHistory().cache(false).complete(),
                                    "logs/" + channel.getName().toLowerCase() + "_ticket_" + (new Random().nextInt(1000)) + ".txt")
                    )
            ).queue();
        }
    }

    public static void ticketLogFile(JDA jda, TextChannel channel, Member member) {
        TextChannel logsChannel = jda.getTextChannelById(Config.getStringValue(ConfigKey.CHANNEL_LOG));

        if (logsChannel != null) {
            generateTicketLogFile(channel, logsChannel);
            logsChannel.sendMessageEmbeds(Embeds.ticketLogEmbed(channel.getName(), (member != null ? member.getAsMention() : "Indisponível"), channel.getName().split("-")[1])).queue();
        } else {
            System.err.println("Logs channel não existe ou é nulo.");
        }
    }
}
