import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.Scanner;

public class DiscordBot {
    private static Logger logger = LogManager.getLogger(DiscordBot.class);
    public static DiscordApi api;
    public static PropertiesLoad properties;
    private static RankmeMessage rankme;

    public static void main(String[] args) {
        properties = new PropertiesLoad();
        api = new DiscordApiBuilder().setToken(properties.getBotToken()).login().join();
        logger.info("Bot working");
        properties.load();

        if (properties.doPlayersCount()) properties.loadPlayersCount();
        if (properties.doPlayersMessage()) properties.loadPlayersMessage();
        if (properties.isDoRankme()) {
            rankme = new RankmeMessage();
            rankme.createChannel();
            api.addMessageCreateListener(rankme);
            logger.info("Rankme message listener ready");
        }

        console();
    }

    public static void console() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("close") || input.equalsIgnoreCase("quit")) {
                for (int i = 0; i < properties.getPlayersCounts().size(); i++) {
                    properties.getPlayersCounts().get(i).deleteChannel();
                }
                if (properties.doPlayersMessage()) properties.getTextChanelCreator().delete();
                if (properties.isDoRankme()) rankme.delete();
                if (properties.isCreateCategory()) properties.getCategoryCreator().delete();
                System.exit(0);
            }
            else {
                String[] message;
                message = input.split(" ");
                if (message.length == 1) {
                    if (message[0].equalsIgnoreCase("pause")) {
                        for (int i = 0; i < properties.getPlayersCounts().size(); i++) {
                            properties.getPlayersCounts().get(i).setWork(false);
                            logger.info("Player Count with id " + i + " paused");
                        }
                        for (int i = 0; i < properties.getPlayersCountsMessage().size(); i++) {
                            properties.getPlayersCountsMessage().get(i).setWork(false);
                            logger.info("Player Message with id " + i + " paused");
                        }
                        api.removeListener(rankme);
                        logger.info("Rankme paused");
                    }
                    if (message[0].equalsIgnoreCase("resume")) {
                        for (int i = 0; i < properties.getPlayersCounts().size(); i++) {
                            properties.getPlayersCounts().get(i).setWork(true);
                            logger.info("Player Count with id " + i + " resumed");
                        }
                        for (int i = 0; i < properties.getPlayersCountsMessage().size(); i++) {
                            properties.getPlayersCountsMessage().get(i).setWork(true);
                            logger.info("Player Message with id " + i + " resumed");
                        }
                        api.addMessageCreateListener(rankme);
                        logger.info("Rankme resumed");
                    }
                }
                else {
                    if (message[0].equalsIgnoreCase("pause")) {
                        if (message[1].equalsIgnoreCase("count")) {
                            properties.getPlayersCounts().get(Integer.parseInt(message[2])).setWork(false);
                            logger.info("Player Count with id " + message[2] + " paused");
                        }
                        if (message[1].equalsIgnoreCase("message")) {
                            properties.getPlayersCountsMessage().get(Integer.parseInt(message[2])).setWork(false);
                            logger.info("Player Message with id " + message[2] + " paused");
                        }
                        if (message[1].equalsIgnoreCase("rankme")) {
                            api.removeListener(rankme);
                            logger.info("Rankem paused");
                        }
                    }
                    if (message[0].equalsIgnoreCase("resume")) {
                        if (message[1].equalsIgnoreCase("count")) {
                            properties.getPlayersCounts().get(Integer.parseInt(message[2])).setWork(true);
                            logger.info("Player Count with id " + message[2] + " resumed");
                        }
                        if (message[1].equalsIgnoreCase("message")) {
                            properties.getPlayersCountsMessage().get(Integer.parseInt(message[2])).setWork(true);
                            logger.info("Player Message with id " + message[2] + " resumed");
                        }
                        if (message[1].equalsIgnoreCase("rankme")) {
                            api.addMessageCreateListener(rankme);
                        }
                    }
                }
            }
        }
    }
}