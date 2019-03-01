import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RankmeMessage implements MessageCreateListener {
    private static Logger logger = LogManager.getLogger(RankmeMessage.class);
    private CompletableFuture<ServerTextChannel> serverTextChannel;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String[] message;
        try {
            if (event.getChannel().getId() != serverTextChannel.join().getId()) return;
            message = event.getMessageContent().split(" ");

            if (message[0].equalsIgnoreCase("!rankme")) {
                Player player = DiscordBot.properties.getSqLconncetion().getScoreBySteam(message[1]);
//                if (player == null) event.getChannel().sendMessage("Unknown player");
//                else {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(player.getName())
                        .addField("Score", player.getScore().toString(), true)
                        .addField("Kills", player.getKils().toString(), true)
                        .addField("Deaths", player.getDeaths().toString(), true)
                        .addField("Assists", player.getAssists().toString(), true)
                        .addField("Shots", player.getShots().toString(), true)
                        .addField("Hits", player.getHits().toString(), true)
                        .addField("HeadShots", player.getHeadshots().toString(), true)
                        .setColor(Color.BLUE)
                        .setAuthor(event.getMessage().getAuthor());
                event.getChannel().sendMessage(embed);
                logger.info("Sended message from RANKME database about player " + message[1]);
//                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            event.getChannel().sendMessage("You forgot to type SteamID!");
        } catch (SQLException e) {
            event.getChannel().sendMessage("Unknown player. Please, try again...");
        }
    }

    public void createChannel() {
        Optional<Server> optionalServer = DiscordBot.api.getServerById(DiscordBot.properties.getServerId());
        Server server = optionalServer.get();
        if (DiscordBot.properties.isCreateCategory()) {
            try {
                serverTextChannel = server.createTextChannelBuilder().setName(DiscordBot.properties.getRankmeChannelname()).setCategory(DiscordBot.properties.getCategoryCreator().getChannelCategoryBuilder().get()).create();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else
            serverTextChannel = server.createTextChannelBuilder().setName(DiscordBot.properties.getRankmeChannelname()).create();
        logger.info("Channel " + DiscordBot.properties.getChanelname() + " created");
    }

    public void delete() {
        serverTextChannel.join().delete();
    }
}
