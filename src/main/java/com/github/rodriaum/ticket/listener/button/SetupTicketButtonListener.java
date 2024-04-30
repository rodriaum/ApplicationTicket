package com.github.rodriaum.ticket.listener.button;

import com.github.rodriaum.ticket.config.Config;
import com.github.rodriaum.ticket.config.key.ConfigKey;
import com.github.rodriaum.ticket.util.Util;
import com.github.rodriaum.ticket.util.embed.Embeds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class SetupTicketButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        boolean isTicketChannel = Arrays.asList("support", "report").contains(componentId);
        int tickets = Objects.requireNonNull(Objects.requireNonNull(event.getGuild())
                .getCategoryById(Config.getStringValue(ConfigKey.CATEGORY_TICKET))).getTextChannels().size();

        if (isTicketChannel && tickets >= ((int) Config.getValue(ConfigKey.MAX_TICKET))) {
            event.reply("Ops! Parece que há mais de 30 tickets, o máximo permitido. Por favor, aguarde um pouco antes de continuar.").queue();
            return;
        }

        switch (componentId) {
            case "support":
                event.replyModal(modal(componentId, ActionRow.of(subject()), ActionRow.of(body()))).queue();
                break;

            case "report":
                TextInput reported = TextInput.create("reported", "Nome ou ID do Denunciado", TextInputStyle.SHORT)
                        .setPlaceholder("rodriaum")
                        .setMinLength(3)
                        .setMaxLength(10)
                        .build();

                TextInput link = TextInput.create("link", "Link Comprovativo", TextInputStyle.SHORT)
                        .setPlaceholder("https://suaprova.com")
                        .setMinLength(3)
                        .setMaxLength(100)
                        .setRequired(false)
                        .build();

                event.replyModal(modal(componentId, ActionRow.of(subject()), ActionRow.of(reported), ActionRow.of(body()), ActionRow.of(link))).queue();
                break;
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        User user = event.getUser();
        String identifier = event.getModalId() + "-" + user.getName();

        if (!Arrays.asList("support", "report").contains(event.getModalId()))
            return;

        Category category = event.getJDA().getCategoryById(Config.getStringValue(ConfigKey.CATEGORY_TICKET));

        if (category != null && Objects.requireNonNull(event.getGuild()).getTextChannelsByName(identifier, false).isEmpty()) {
            TextChannel ticket = category.createTextChannel(identifier).complete();

            ticket.getPermissionContainer().getManager()
                    .putMemberPermissionOverride(user.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES), Collections.emptySet())
                    .queue();

            event.reply(user.getAsMention() + ", seu ticket foi aberto! " + ticket.getAsMention()).setEphemeral(true).queue();

            String subject = event.getValue("subject") != null ? Objects.requireNonNull(event.getValue("subject")).getAsString() : "";
            String body = event.getValue("body") != null ? Objects.requireNonNull(event.getValue("body")).getAsString() : "";

            String reported = event.getValue("reported") != null ? Objects.requireNonNull(event.getValue("reported")).getAsString() : "";
            String link = event.getValue("link") != null ? Objects.requireNonNull(event.getValue("link")).getAsString() : "";

            ticket.sendMessageEmbeds(Objects.requireNonNull(Embeds.ticketChatEmbed(user, ticket, subject, reported, body, link)))
                    .addActionRow(Button.secondary("close", "\uD83D\uDD12 Fechar Ticket"))
                    .queue();
        } else {
            event.reply(Util.formatToUtf8("Você já possui um ticket aberto! " + user.getAsMention())).setEphemeral(true).queue();
        }
    }

    private Modal modal(String componentId, ActionRow... rows) {
        return Modal.create(componentId, "Ticket")
                .addComponents(rows)
                .build();
    }

    /** @apiNote Seu ID in-game */
    private TextInput subject() {
        return TextInput.create("subject", "Seu Nick ou ID", TextInputStyle.SHORT)
                .setPlaceholder("rodriaum")
                .setMinLength(3)
                .setMaxLength(16)
                .build();
    }

    /** @apiNote Sua Descrição do Motivo */
    private TextInput body() {
        return TextInput.create("body", Util.formatToUtf8("Insira a Descrição"), TextInputStyle.PARAGRAPH)
                .setPlaceholder("Insira o seu problema aqui.")
                .setMinLength(5)
                .setMaxLength(100)
                .build();
    }
}
