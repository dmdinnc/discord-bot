package com.dmdinnc.discordbot.listeners;

import com.dmdinnc.discordbot.ConfigurationManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ExampleListener extends ListenerAdapter {

    private static String TEST_GUILD_ID;
    private static String TEST_CHANNEL_ID;

    public ExampleListener(ConfigurationManager configurationManager) {
        //Load configuration from environment
        TEST_GUILD_ID = configurationManager.getTestGuildId();
        TEST_CHANNEL_ID = configurationManager.getTestChannelId();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentDisplay();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
        if (content.equals("~ping"))
        {
            MessageChannel channel = event.getChannel();
            System.out.println("~ping command received in " + channel.getId() + " channel in " + event.getGuild().getId() + " guild.");
            if (event.getGuild().getId().equals(TEST_GUILD_ID) &&
                    channel.getId().equals(TEST_CHANNEL_ID)) {
                channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
            }
        }
        System.out.println("Message received: " + message.getContentDisplay());
    }
}
