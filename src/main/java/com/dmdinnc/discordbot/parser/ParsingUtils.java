package com.dmdinnc.discordbot.parser;

import org.intellij.lang.annotations.RegExp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtils {

    private static final Pattern MESSAGE_TIME_REGEX = Pattern.compile("(([0-9]{1,2}):)?([0-9]{1,2}):([0-9]{2})");

    private static final Pattern LINK_REGEX = Pattern.compile(".*https:\\/\\/www\\.nytimes\\.com\\/badges\\/games\\/mini\\.html" +
            "\\?d=([0-9]{4}-[0-9]{2}-[0-9]{2})&t=([0-9]+)&c.*");

    private static final Pattern MESSAGE_REGEX = Pattern.compile(".*I solved the ([0-9]{1,2}\\/[0-9]{1,2}\\/[0-9]{4}) New York Times Mini Crossword in ([0-9]{0,2}:?[0-9]{1,2}:[0-9]{2})!.*");

    public static MiniTimeData getMiniTime(String message) {
        String msg = message.replaceAll("(\r|\n)", "");
        Matcher matcher = LINK_REGEX.matcher(msg);
        if (matcher.matches()) {
            try {
                return new MiniTimeData(matcher.group(1),
                        Integer.valueOf(matcher.group(2)));
            } catch (Exception e) {
                System.out.println("Error parsing link: " + msg);
                e.printStackTrace();
                return new MiniTimeData("1980-01-01", -1);
            }
        }

        Matcher matcherMsg = MESSAGE_REGEX.matcher(msg);
        if (matcherMsg.matches()) {
            try {
                return new MiniTimeData(formatDate(matcherMsg.group(1)),
                        parseTime(matcherMsg.group(2)));
            } catch (Exception e) {
                System.out.println("Error parsing message: " + msg);
                e.printStackTrace();
                return new MiniTimeData("1980-01-01", -1);
            }
        }

        //TODO Attempt style 2
        System.out.println("No match found for message: " + msg);
        return new MiniTimeData("1980-01-01", -1);
    }

    private static int parseTime(String time) {
        System.out.println("Parsing time: " + time);
        Matcher data = MESSAGE_TIME_REGEX.matcher(time);
        if (!data.matches()) {
            return -1;
        }
        int hours = data.group(2) != null && !data.group(2).equals("") ? Integer.parseInt(data.group(2)) : 0;
        int minutes = Integer.parseInt(data.group(3));
        int seconds = Integer.parseInt(data.group(4));
        return (hours * 3600) + (minutes * 60) + seconds;
    }

    private static String formatDate(String date) {
        System.out.println("Parsing date: " + date);
        Pattern datePattern = Pattern.compile("([0-9]{1,2})\\/([0-9]{1,2})\\/([0-9]{4})");
        Matcher data = datePattern.matcher(date);
        if (data.matches()) {
            return data.group(3) + "-" + data.group(1) + "-" + data.group(2);
        }
        return "1980-01-01";
    }

    public static class MiniTimeData {
        private String date;
        private int time;

        public MiniTimeData(String date, int time) {
            this.date = date;
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public int getTime() {
            return time;
        }
    }


}
