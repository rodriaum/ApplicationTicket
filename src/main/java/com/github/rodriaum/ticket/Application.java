package com.github.rodriaum.ticket;

import com.github.rodriaum.ticket.command.TicketCommand;
import com.github.rodriaum.ticket.config.Config;
import com.github.rodriaum.ticket.config.key.ConfigKey;
import com.github.rodriaum.ticket.listener.button.SetupTicketButtonListener;
import com.github.rodriaum.ticket.listener.button.TicketButtonListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Application implements EventListener {

    public static void main(String[] args) throws InterruptedException {
        OnlineStatus status = OnlineStatus.fromKey(Config.getStringValue(ConfigKey.ONLINE_STATUS));

        JDABuilder build = JDABuilder.create(Config.getStringValue(ConfigKey.TOKEN), EnumSet.allOf(GatewayIntent.class))
                .setStatus(status == null || status.equals(OnlineStatus.UNKNOWN) ? OnlineStatus.DO_NOT_DISTURB : status)
                .addEventListeners(
                        // This
                        new Application(),
                        // Listeners
                        new SetupTicketButtonListener(),
                        new TicketButtonListener(),
                        // Commands
                        new TicketCommand()
                );

        Config.init();

        JDA jda = build.build();

        jda.updateCommands().addCommands(
                Commands.slash("ticketsetup", "Crie a embed de ticket.")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                Commands.slash("closeticket", "Elimine o ticket atual.")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL))
        ).queue();

        jda.awaitReady();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            System.out.println("JDA iniciado com sucesso.");
        }
    }
}