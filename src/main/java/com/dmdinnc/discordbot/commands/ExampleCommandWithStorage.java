package com.dmdinnc.discordbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExampleCommandWithStorage extends ListenerAdapter {
    List<String> words = new ArrayList<>();

    private final String cacheFile = "cache.json";

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Initializing command 'cached_say'");
        JDA bot = event.getJDA();
        bot.updateCommands().addCommands(
                Commands.slash("flig", "Make the bot say something")
                        .addSubcommands(
                            SubcommandData.fromData(
                                    Commands.slash("add", "Add a word to the cache")
                                        .addOption(OptionType.STRING, "word", "The word to add", true)
                                        .toData()),
                                SubcommandData.fromData(
                                    Commands.slash("list", "List all words in the cache")
                                            .toData())
                        )
        ).queue();

        // Check to see if the cache.json file exists
        if (new File(cacheFile).exists()) {
            // Load the cache from the file
            try {
                JSONParser parser = new JSONParser();
                JSONObject obj;
                obj = (JSONObject) parser.parse(new FileReader(cacheFile));
                JSONArray wordObj = (JSONArray) obj.get("words");
                Iterator i = wordObj.stream().iterator();
                while (i.hasNext()) {
                    words.add((String) i.next());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("unreadable file, starting new");
            }
        } else  {
            System.out.println("No file, starting new");
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("flig")) {
            event.deferReply().queue(); // defer the reply to ensure the bot doesn't time out
            InteractionHook hook = event.getHook();
            if (event.getSubcommandName().equals("add")) {
                try {
                    String word = event.getOption("word", "blank", OptionMapping::getAsString);
                    if (!words.contains(word)) {
                        words.add(word);
                        JSONObject obj = new JSONObject();
                        obj.put("words", JSONArray.toJSONString(words));
                        FileWriter file = new FileWriter(cacheFile);
                        file.write(obj.toString());
                        file.close();
                        hook.editOriginal("Unique response! '" + word + "'").queue();
                    } else {
                        hook.editOriginal("non-unique response! '" + word + "'").queue();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    hook.editOriginal("An error occurred while executing the command!").queue();
                }
            } else if (event.getSubcommandName().equals("list")) {
                hook.editOriginal("Words: " + words).queue();
            }
        }
    }
}
