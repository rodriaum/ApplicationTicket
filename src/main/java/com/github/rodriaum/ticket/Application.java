package com.github.rodriaum.ticket;

import com.github.rodriaum.ticket.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Application implements EventListener {

    public static void main(String[] args) throws InterruptedException {
        OnlineStatus status = Config.getStatus();

        JDABuilder build = JDABuilder.create(Config.getToken(), EnumSet.allOf(GatewayIntent.class))
                .addEventListeners(new Application())
                .setStatus(status == null || status.equals(OnlineStatus.UNKNOWN) ? OnlineStatus.DO_NOT_DISTURB : status);

        Config.init();

        JDA jda = build.build();

        jda.awaitReady();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            System.out.println("JDA iniciado com sucesso.");
        }
    }
}