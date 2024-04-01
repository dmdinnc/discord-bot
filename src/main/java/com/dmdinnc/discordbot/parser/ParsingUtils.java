package com.dmdinnc.discordbot.parser;

public class ParsingUtils {

    public static int getMiniTime(String message) {
        if (!message.contains("www.nytimes.com")) {
            return 0;
        }
        // Attempt style 1
        if (message.contains("badges")) {
            String[] splitStrings = message.split("&");
            for (String s : splitStrings) {
                if (s.charAt(0) == 't' && s.charAt(1) == '=') {
                    // We found the time
                    String[] splitString2 = s.split("t=");
                    return Integer.valueOf(splitString2[0]);
                }
            }
        }

        //TODO Attempt style 2

        return 0;
    }


}
