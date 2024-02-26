package com.github.rodriaum.ticket.command;

import com.github.rodriaum.ticket.util.Util;
import com.github.rodriaum.ticket.util.embed.Embeds;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicketCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

        switch (event.getName()) {
            case "ticketsetup":
                event.replyEmbeds(Embeds.ticketEmbed(Objects.requireNonNull(event.getGuild())))
                        .addActionRow(ticketSetupRow().getComponents())
                        .queue();
                break;

            case "closeticket":
                if (Arrays.asList("support", "certificate", "report").contains(event.getChannel().getName().split("-")[0])) {
                    channel.delete().queueAfter(5, TimeUnit.SECONDS);

                    Util.ticketLogFile(event.getJDA(), channel, event.getMember());

                    event.reply("**Fechando o ticket em 5s... Esperamos que seu problema esteja resolvido!**").queue();
                } else {
                    event.reply(Util.formatToUtf8("Não foi possível eliminar o canal porque não é considerado um ticket.")).queue();
                }
                break;
        }
    }

    private ActionRow ticketSetupRow() {
        return ActionRow.of(
                Button.secondary("support", "\uD83D\uDD27 Suporte"),
                Button.secondary("report", "\u274C " + Util.formatToUtf8("Denúncias")));
    }
}