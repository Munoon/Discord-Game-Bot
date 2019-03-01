import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesLoad {
    private List<PlayersCount> playersCounts = new ArrayList<>();
    private List<PlayersMessage> playersCountsMessage = new ArrayList<>();
    private static Logger logger = LogManager.getLogger(PropertiesLoad.class);
    private String BotToken, chanelname, rankmeChannelname, categoryName;
    private long ServerId;
    private boolean doPlayersCount, doPlayersMessage, showPlayersScore, doRankme, createCategory = false;
    private TextChanelCreator textChanelCreator;
    private Properties properties = new Properties();
    private FileInputStream in;
    private int serverInfoUpdate, playersInfoUpdate;
    private SQLconncetion sqLconncetion;
    private CategoryCreator categoryCreator;

    public PropertiesLoad() {
        try {
            in = new FileInputStream("config.properties");
            properties.load(in);
        } catch (FileNotFoundException e) {
            logger.error("FILE NOT FOUND EXCEPTION: " + e);
        } catch (IOException e) {
            logger.error("IO EXCEPTION: " + e);
        }

        BotToken = properties.getProperty("BotToken");
        ServerId = Long.parseLong(properties.getProperty("ServerID"));
    }

    public void load() {
        if (!properties.getProperty("CreateCategory").equals("false")) {
            categoryName = properties.getProperty("CreateCategory");
            createCategory = true;
            categoryCreator = new CategoryCreator();
        }

        String doPlayersCountString = properties.getProperty("DoPlayersCount", "false");
        if (doPlayersCountString.equalsIgnoreCase("yes") || doPlayersCountString.equalsIgnoreCase("true")) doPlayersCount = true;
        else if (doPlayersCountString.equalsIgnoreCase("no") || doPlayersCountString.equalsIgnoreCase("false")) doPlayersCount = false;
        else logger.error("Unknown format in DoPlayersCount setting.");

        if (doPlayersCount) {
            String server = properties.getProperty("PlayersCountServers");
            addPlayersCount(server);
            serverInfoUpdate = Integer.parseInt(properties.getProperty("ServersInfoUpdateDuration"));
            playersInfoUpdate = Integer.parseInt(properties.getProperty("PlayersInfoUpdateDuration"));
        }

        String doPlayersMessageString = properties.getProperty("DoPlayersMessage", "false");
        if (doPlayersMessageString.equalsIgnoreCase("yes") || doPlayersMessageString.equalsIgnoreCase("true")) doPlayersMessage = true;
        else if (doPlayersMessageString.equalsIgnoreCase("no") || doPlayersMessageString.equalsIgnoreCase("false")) doPlayersMessage = false;
        else logger.error("Unknown format in DoPlayersMessage setting.");

        if (doPlayersMessage) {
            chanelname = properties.getProperty("ChannelName", "servers-monitoring");
            String showPlayersScoreString = properties.getProperty("ShowPlayersScore", "true");
            if (showPlayersScoreString.equalsIgnoreCase("yes") || showPlayersScoreString.equalsIgnoreCase("true")) showPlayersScore = true;
            else if (showPlayersScoreString.equalsIgnoreCase("no") || showPlayersScoreString.equalsIgnoreCase("false")) showPlayersScore = false;
            else logger.error("Unknown format in ShowPlayersScoreString setting.");

            String servers = properties.getProperty("PlayersCountMessages");
            addPlayersMessage(servers);
            serverInfoUpdate = Integer.parseInt(properties.getProperty("ServersInfoUpdateDuration"));
            playersInfoUpdate = Integer.parseInt(properties.getProperty("PlayersInfoUpdateDuration"));
        }

        String doRankmeString = properties.getProperty("DoRankMe", "false");
        if (doRankmeString.equalsIgnoreCase("yes") || doRankmeString.equalsIgnoreCase("true")) doRankme = true;
        else if (doRankmeString.equalsIgnoreCase("no") || doRankmeString.equalsIgnoreCase("false")) doRankme = false;
        else logger.error("Unknown format in DoRankMe setting.");

        if (doRankme) {
            sqLconncetion = new SQLconncetion("jdbc:mysql://" + properties.getProperty("Address") + "/" + properties.getProperty("Table") + "?autoReconnect=true&useSSL=false", properties.getProperty("Username"), properties.getProperty("Password"));
            rankmeChannelname = properties.getProperty("RankmeChannelName");
        }
    }

    public void addPlayersMessage(String servers) {
        List<String> serversString = new ArrayList<>();
        while (servers.contains(",")) {
            serversString.add(servers.substring(0, servers.indexOf(",")));
            servers = servers.substring(servers.indexOf(", ") + 1);
        }
        serversString.add(servers.substring(1));

        textChanelCreator = new TextChanelCreator(getServerId());
        for (int i = 0; i < serversString.size(); i++) {
            String param = serversString.get(i);
            String ip = param.substring(0, param.indexOf(":"));
            param = param.substring(param.indexOf(":") + 1);
            String port = param.substring(0, param.indexOf(":"));
            String name = param.substring(param.indexOf(":") + 1);
            playersCountsMessage.add(new PlayersMessage(ip, Integer.parseInt(port), name, textChanelCreator.getServerTextChannel()));
        }
    }

    public void loadPlayersMessage() {
        for (int i = 0; i < playersCountsMessage.size(); i++) {
            playersCountsMessage.get(i).createMessage();
            playersCountsMessage.get(i).start();
        }
    }

    public void loadPlayersCount() {
        for (int i = 0; i < playersCounts.size(); i++) {
            playersCounts.get(i).start();
        }
    }

    private void addPlayersCount(String servers) {
        List<String> serversString = new ArrayList<>();
        while (servers.contains(",")) {
            serversString.add(servers.substring(0, servers.indexOf(",")));
            servers = servers.substring(servers.indexOf(", ") + 1);
        }
        serversString.add(servers.substring(1));

        for (int i = 0; i < serversString.size(); i++) {
            String param = serversString.get(i);
            String ip = param.substring(0, param.indexOf(":"));
            param = param.substring(param.indexOf(":") + 1);
            String port = param.substring(0, param.indexOf(":"));
            String name = param.substring(param.indexOf(":") + 1);
            playersCounts.add(new PlayersCount(ip, Integer.parseInt(port), name, ServerId));
        }
        for (int i = 0; i < playersCounts.size(); i++) {
            playersCounts.get(i).createChannel();
        }
    }

    public TextChanelCreator getTextChanelCreator() {
        return textChanelCreator;
    }

    public List<PlayersCount> getPlayersCounts() {
        return playersCounts;
    }

    public List<PlayersMessage> getPlayersCountsMessage() {
        return playersCountsMessage;
    }

    public boolean doPlayersMessage() {
        return doPlayersMessage;
    }

    public boolean doPlayersCount() {
        return doPlayersCount;
    }

    public String getBotToken() {
        return BotToken;
    }

    public long getServerId() {
        return ServerId;
    }

    public void setPlayerMessageWork(boolean playerMessageWork) {
        this.doPlayersMessage = playerMessageWork;
    }

    public int getServerInfoUpdate() {
        return serverInfoUpdate;
    }

    public int getPlayersInfoUpdate() {
        return playersInfoUpdate;
    }

    public String getChanelname() {
        return chanelname;
    }

    public boolean isShowPlayersScore() {
        return showPlayersScore;
    }

    public SQLconncetion getSqLconncetion() {
        return sqLconncetion;
    }

    public boolean isDoRankme() {
        return doRankme;
    }

    public String getRankmeChannelname() {
        return rankmeChannelname;
    }

    public CategoryCreator getCategoryCreator() {
        return categoryCreator;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean isCreateCategory() {
        return createCategory;
    }
}
