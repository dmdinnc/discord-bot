package com.dmdinnc.discordbot;

public class ConfigurationManager {

    private static String BOT_TOKEN;

    private static String TEST_GUILD_ID;

    private static String TEST_CHANNEL_ID;

    public ConfigurationManager() {
        //Load configuration from environment
        BOT_TOKEN = System.getenv("BOT_TOKEN");

        TEST_GUILD_ID = System.getenv("GUILD_ID");
        System.out.println("GUILD_ID: " + TEST_GUILD_ID);

        TEST_CHANNEL_ID = System.getenv("CHANNEL_ID");
        System.out.println("CHANNEL_ID: " + TEST_CHANNEL_ID);
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public String getTestGuildId() {
        return TEST_GUILD_ID;
    }

    public String getTestChannelId() {
        return TEST_CHANNEL_ID;
    }
}
