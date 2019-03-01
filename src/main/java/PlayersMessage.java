import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayersMessage extends Thread {
    private String ip;
    private int port;
    private String name;
    private static Logger logger = LogManager.getLogger(PlayersMessage.class);
    private long serverId;
    private CompletableFuture<ServerTextChannel> serverTextChannel;
    private boolean work = true;
    private MessageBuilder messageBuilder;
    private CompletableFuture<Message> message;

    public PlayersMessage(String ip, int port, String name, CompletableFuture<ServerTextChannel> serverTextChannel) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.serverTextChannel = serverTextChannel;
    }

    @Override
    public void run() {
        while (work) {
            List<SourcePlayer> list = getPlayers();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name + "\n");
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).equals("")) {
                    if (DiscordBot.properties.isShowPlayersScore()) {
                        StringBuilder spaces = new StringBuilder();
                        int needSpaces = 30 - list.get(i).getName().length();
                        if (needSpaces < 0) needSpaces = 5;
                        for (int j = 0; j < needSpaces; j++) {
                            spaces.append(" ");
                        }
                        stringBuilder.append(list.get(i).getName() + spaces + list.get(i).getScore() + "\n");
                    } else stringBuilder.append(list.get(i).getName() + "\n");
                }
            }
            message.join().edit("```\n" + stringBuilder + "```");

            logger.info("Message about server " + name +  " updated");
            try {
                Thread.sleep(DiscordBot.properties.getPlayersInfoUpdate());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!work) logger.info("Updates in channel " + name + " paused");
    }

    public void createMessage() {
        List<SourcePlayer> list = getPlayers();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name + "\n");
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals("")) {
                if (DiscordBot.properties.isShowPlayersScore()) {
                    StringBuilder spaces = new StringBuilder();
                    int needSpaces = 30 - list.get(i).getName().length();
                    if (needSpaces < 0) needSpaces = 5;
                    for (int j = 0; j < needSpaces; j++) {
                        spaces.append(" ");
                    }
                    stringBuilder.append(list.get(i).getName() + spaces + list.get(i).getScore() + "\n");
                } else stringBuilder.append(list.get(i).getName() + "\n");
            }
        }
        message = serverTextChannel.join().sendMessage("```\n" + stringBuilder + "```");
    }

    private List<SourcePlayer> getPlayers() {
        List<SourcePlayer> result = new ArrayList<>();
        try (SourceQueryClient sourceQueryClient = new SourceQueryClient()) {
            InetSocketAddress serverAddress = new InetSocketAddress(ip, port);
            CompletableFuture<List<SourcePlayer>> playerInfoFuture = sourceQueryClient.getPlayers(serverAddress);
            playerInfoFuture.whenComplete((List<SourcePlayer> players, Throwable playerError) -> {
                for (int i = 0; i < players.size(); i++) {
                    result.add(players.get(i));
                }
            }).join();
        } catch (Exception e) {
            logger.error("Exception error: ", e);
        }
        return result;
    }

    public void setWork(boolean work) {
        this.work = work;
    }
}
