package File;

import File.database.SQLiteDataSource;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// For "listening" events
public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull final GuildMessageReceivedEvent event) {
        final User user = event.getAuthor();
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length == 1 && !event.getMessage().getMentionedMembers().isEmpty()) {
            if (event.getMessage().getMentionedMembers().get(0).getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
                if(args[0].equalsIgnoreCase("<@!" + event.getJDA().getSelfUser().getId() + ">")||args[0].equalsIgnoreCase("<@!" + event.getJDA().getSelfUser().getId() + ">")) {
                    event.getChannel().sendMessage("My prefix is " + getPrefix(event.getGuild().getIdLong())).queue();
                }
            }
        }
        if(user.isBot() || event.isWebhookMessage()){
            return;
        }
        final long guildID = event.getGuild().getIdLong();
        String prefix = Prefix.PREFIXES.computeIfAbsent(guildID, this::getPrefix);
        String raw = event.getMessage().getContentRaw();
        if(raw.equalsIgnoreCase(prefix+"shutdown")){
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
            return;
        }
        if(raw.startsWith(prefix)){
            manager.handle(event, prefix);
        }

    }
    private String getPrefix(long guildId){
        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLite
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = SQLiteDataSource
                    .getConnection()
                    // language=SQLite
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ".";
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }
        if(event.getMessage().getContentRaw().equals("")){
            System.out.println("-Image was sent by " + event.getAuthor().getName());
        }
        else {
            System.out.println(event.getMessage().getContentRaw() + "\n-Sent by " + event.getAuthor().getAsTag());
        }
    }

}

