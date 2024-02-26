package listener.button;

import com.github.rodriaum.ticket.util.Util;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class TicketButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

        if (event.getComponentId().equals("close")) {
            channel.delete().queueAfter(5, TimeUnit.SECONDS);

            Util.ticketLogFile(event.getJDA(), channel, event.getMember());

            event.reply("**Fechando o ticket em 5s... Esperamos que seu problema esteja resolvido!**").queue();
        }
    }
}