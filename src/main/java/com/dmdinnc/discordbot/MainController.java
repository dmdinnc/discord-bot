package com.dmdinnc.discordbot;

import com.dmdinnc.discordbot.commands.ExampleCommand;
import com.dmdinnc.discordbot.listeners.ExampleListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MainController {

    private static final ConfigurationManager configurationManager = new ConfigurationManager();

    private static final ExampleCommand exampleCommand = new ExampleCommand();

    public static void main(String[] arguments) throws Exception {
        assert configurationManager.getBotToken() != null;
        String BOT_TOKEN = configurationManager.getBotToken();

        JDA api = JDABuilder.createLight(BOT_TOKEN, GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).build();
        api.addEventListener(new ExampleListener(configurationManager));
        api.addEventListener(exampleCommand);
    }
}
