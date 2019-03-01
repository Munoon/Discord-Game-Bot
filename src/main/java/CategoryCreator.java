import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.server.Server;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CategoryCreator {
    private CompletableFuture<ChannelCategory> channelCategoryBuilder;
    private static Logger logger = LogManager.getLogger(TextChanelCreator.class);

    public CategoryCreator() {
        Optional<Server> optionalServer = DiscordBot.api.getServerById(DiscordBot.properties.getServerId());
        Server server = optionalServer.get();

        channelCategoryBuilder = server.createChannelCategoryBuilder().setName(DiscordBot.properties.getCategoryName()).create();
        logger.info("Category " +  DiscordBot.properties.getCategoryName() + " created");
    }

    public void delete() {
        channelCategoryBuilder.join().delete();
    }

    public CompletableFuture<ChannelCategory> getChannelCategoryBuilder() {
        return channelCategoryBuilder;
    }
}
