package com.dmdinnc.discordbot.listeners;

import com.dmdinnc.discordbot.ConfigurationManager;
import com.dmdinnc.discordbot.parser.ParsingUtils;
import com.dmdinnc.discordbot.parser.ParsingUtils.MiniTimeData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WordleListener extends ListenerAdapter {

    private static String TEST_GUILD_ID;
    private static String TEST_CHANNEL_ID;

    public WordleListener(ConfigurationManager configurationManager) {
        //Load configuration from environment
        TEST_GUILD_ID = configurationManager.getTestGuildId();
        TEST_CHANNEL_ID = configurationManager.getTestChannelId();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentDisplay();
        MessageChannel channel = event.getChannel();

        if (event.getGuild().getId().equals(TEST_GUILD_ID) &&
                channel.getId().equals(TEST_CHANNEL_ID)) {
            System.out.println("Mini time command received in " + channel.getId() + " channel in " + event.getGuild().getId() + " guild.");
            MiniTimeData data = ParsingUtils.getMiniTime(content);
            if (data.getTime() == -1) {
                System.out.println("Invalid mini time: " + content);
                return;
            }
            String author = message.getAuthor().getGlobalName();
            System.out.println("Mini time: " + data.getTime() + " by " + author);
            channel.sendMessage("Mini time: " + data.getTime() + " by " + author).queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
        }
    }
}
