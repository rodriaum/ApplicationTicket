package com.github.rodriaum.ticket.util.embed;

import com.github.rodriaum.ticket.config.Config;
import com.github.rodriaum.ticket.config.key.ConfigKey;
import com.github.rodriaum.ticket.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;

public class Embeds {

    public static MessageEmbed ticketEmbed(Guild guild) {
        return new EmbedBuilder()
                .setTitle("Ticket - " + guild.getName())
                .setDescription(Util.formatToUtf8("Seja bem-vindo à " + guild.getName() + "!\nEstamos aqui para garantir sua experiência imersiva e divertida no servidor.\nEm caso de dúvidas ou problemas, estamos à disposição para ajudar."))
                .setColor(Color.RED)
                .setFooter(LocalDate.now().getYear() + " | " + guild.getName(), Config.getStringValue(ConfigKey.LOGO_LINK))
                .build();
    }

    public static MessageEmbed ticketChatEmbed(User user, TextChannel channel, String id, String reported, String reason, String link) {
        return new EmbedBuilder()
                .setTitle(Util.formatName(channel.getName().split("-")[0]) + ": Novo Ticket")
                .setDescription(Util.formatToUtf8("O jogador " + user.getAsMention() + " criou um novo ticket.\n\n```" + (!id.isEmpty() ? "ID: " + id + "\n" : "") + (!reported.isEmpty() ? "ID Reportado: " + reported + "\n" : "") + (!reason.isEmpty() ? "Motivo: " + reason + "\n" : "") + (!link.isEmpty() ? "Link: " + link + "\n" : "") + "\n```Aguarde a resposta da equipe, por favor, sem marcá-los.\n" + Objects.requireNonNull(channel.getGuild().getRoleById(Config.getStringValue(ConfigKey.STAFF_ROLE))).getAsMention()))
                .setColor(Color.RED)
                .setFooter(LocalDate.now().getYear() + " | " + channel.getGuild().getName(), Config.getStringValue(ConfigKey.LOGO_LINK))
                .build();
    }

    public static MessageEmbed ticketLogEmbed(String channelName, String ticketAuthor, String closeAuthor) {
        return new EmbedBuilder()
                .setTitle("\uD83C\uDFAB Ticket Fechado")
                .setDescription(Util.formatToUtf8("**Canal**\n" + channelName + "\n\n**Criador**\n" + ticketAuthor + "\n\n**Fechado Por**\n" +closeAuthor + "\n\n`Obs: O arquivo log esta acima.`"))
                .setColor(Color.RED)
                .setFooter(LocalDate.now().toString(), Config.getStringValue(ConfigKey.LOGO_LINK))
                .build();
    }
}
