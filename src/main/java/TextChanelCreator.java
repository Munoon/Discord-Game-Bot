import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.logging.ExceptionLogger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TextChanelCreator {
    private long serverId;
    private CompletableFuture<ServerTextChannel> serverTextChannel;
    private static Logger logger = LogManager.getLogger(TextChanelCreator.class);

    public TextChanelCreator(long serverId) {
        this.serverId = serverId;
        createChannel();
    }

    public void createChannel() {
        Optional<Server> optionalServer = DiscordBot.api.getServerById(serverId);
        Server server = optionalServer.get();
        try {
            if (DiscordBot.properties.isCreateCategory())
                serverTextChannel = server.createTextChannelBuilder()
                        .setName(DiscordBot.properties.getChanelname())
                        .addPermissionOverwrite(server.getEveryoneRole(), new PermissionsBuilder()
                                .setAllowed(PermissionType.READ_MESSAGES, PermissionType.READ_MESSAGE_HISTORY)
                                .setDenied(PermissionType.SEND_MESSAGES, PermissionType.ADD_REACTIONS)
                                .build())
                        .setCategory(DiscordBot.properties.getCategoryCreator().getChannelCategoryBuilder().get())
                        .create()
                        .exceptionally(ExceptionLogger.get());
            if (!DiscordBot.properties.isCreateCategory())
                serverTextChannel = server.createTextChannelBuilder()
                        .setName(DiscordBot.properties.getChanelname())
                        .addPermissionOverwrite(server.getEveryoneRole(), new PermissionsBuilder()
                                .setAllowed(PermissionType.READ_MESSAGES, PermissionType.READ_MESSAGE_HISTORY)
                                .setDenied(PermissionType.SEND_MESSAGES, PermissionType.ADD_REACTIONS)
                                .build())
                        .create()
                        .exceptionally(ExceptionLogger.get());
            logger.info("Text Channel created");
            // TODO add category supporting
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    public void delete() {
        serverTextChannel.join().delete();
    }

    public CompletableFuture<ServerTextChannel> getServerTextChannel() {
        return serverTextChannel;
    }
}
