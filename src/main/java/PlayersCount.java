import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelCategoryBuilder;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.logging.ExceptionLogger;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayersCount extends Thread {
    private String ip;
    private int port;
    private String name;
    private static Logger logger = LogManager.getLogger(PlayersCount.class);;
    private ServerVoiceChannel serverVoiceChannel;
    private long serverId;
    private boolean work = true;
    private Server server;

    public PlayersCount(String ip, int port, String name, long serverId) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.serverId = serverId;
    }

    public void createChannel() {
        Optional<Server> optionalServer = DiscordBot.api.getServerById(serverId);
        server = optionalServer.get();
        try {
            if (DiscordBot.properties.isCreateCategory())
                serverVoiceChannel = server.createVoiceChannelBuilder()
                        .setName(String.format(name, getCountOfPlayers()))
                        .addPermissionOverwrite(server.getEveryoneRole(), new PermissionsBuilder()
                                .setDenied(PermissionType.CONNECT)
                                .build())
                        .setCategory(DiscordBot.properties.getCategoryCreator().getChannelCategoryBuilder().get())
                        .create()
                        .exceptionally(ExceptionLogger.get()).get();
            if (!DiscordBot.properties.isCreateCategory())
                serverVoiceChannel = server.createVoiceChannelBuilder()
                        .setName(String.format(name, getCountOfPlayers()))
                        .addPermissionOverwrite(server.getEveryoneRole(), new PermissionsBuilder()
                                .setDenied(PermissionType.CONNECT)
                                .build())
                        .create()
                        .exceptionally(ExceptionLogger.get()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        logger.info("Chanel " + name +  " created");
    }

    @Override
    public void run() {
        while (work) {
            serverVoiceChannel.updateName(String.format(name, getCountOfPlayers())).exceptionally(ExceptionLogger.get());
            logger.info("Chanel " + name +  " updated");
            try {
                Thread.sleep(DiscordBot.properties.getServerInfoUpdate());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!work) logger.info("Updates in channel " + name + " paused");
    }

    public void deleteChannel() {
        serverVoiceChannel.delete();
    }

    private int getCountOfPlayers() {
        AtomicInteger result = new AtomicInteger();
        try (SourceQueryClient sourceQueryClient = new SourceQueryClient()) {
            InetSocketAddress serverAddress = new InetSocketAddress(ip, port);
            CompletableFuture<List<SourcePlayer>> playerInfoFuture = sourceQueryClient.getPlayers(serverAddress);
            playerInfoFuture.whenComplete((List<SourcePlayer> players, Throwable playerError) -> {
                result.set(players.size());
            }).join();
        } catch (NullPointerException e) {
            return 0;
        } catch (Exception e) {
            logger.error("Exception error: ", e);
        }
        return result.get();
    }

    public void setWork(boolean work) {
        this.work = work;
    }
}
