package com.dmdinnc.discordbot;

import com.dmdinnc.discordbot.commands.ExampleCommand;
import com.dmdinnc.discordbot.listeners.ExampleListener;
import com.dmdinnc.discordbot.listeners.WordleListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.time.Duration;

public class MainController {

    private static final ConfigurationManager configurationManager = new ConfigurationManager();

    public static void main(String[] arguments) throws Exception {
        assert configurationManager.getBotToken() != null;
        String BOT_TOKEN = configurationManager.getBotToken();

        JDA api = JDABuilder.createLight(BOT_TOKEN, GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).build();

        //Message listeners
        api.addEventListener(new ExampleListener(configurationManager));
        api.addEventListener(new WordleListener(configurationManager));

        //Command listeners
        api.addEventListener(new ExampleCommand());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down bot...");
            try {
                if (api.awaitShutdown(Duration.ofSeconds(5))) {
                    System.out.println("Bot has been shut down successfully.");
                } else {
                    api.shutdownNow();
                    System.out.println("Bot has been shut down forcefully.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                api.shutdownNow();
            }
        }));
    }
}
