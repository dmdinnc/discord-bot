package com.dmdinnc.discordbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExampleCommand extends ListenerAdapter {
    private String[] words = new String[]{"hello!", "good day!", "Test!"};

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Initializing command 'say'");
        JDA bot = event.getJDA();
        bot.updateCommands().addCommands(
                Commands.slash("say", "Make the bot say something")
                        .addOption(OptionType.STRING, "content", "The content to say", true, true)
        ).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        System.out.println("Command received: " + event.getName());
        if (event.getName().equals("say")) {
            try {
                if (event.getOption("content") == null) {
                    event.reply("You need to provide content!").setEphemeral(true).queue(); // reply immediately
                } else {
                    event.reply(event.getOption("content").getAsString()).queue();
                }
            } catch (NullPointerException e) {
                event.reply("An error occurred while executing the command!").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        System.out.println("Auto-complete received: " + event.getName());
        if (event.getName().equals("say") && event.getFocusedOption().getName().equals("content")) {
            List<Command.Choice> options = Stream.of(words)
                    //.filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
}
