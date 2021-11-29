package File;

import File.database.SQLiteDataSource;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;
import java.sql.SQLException;

// Can be cleaned up, probably should be
public class Bot {
    public Bot() throws LoginException, SQLException {
        SQLiteDataSource.getConnection();
        OnlineStatus status = OnlineStatus.DO_NOT_DISTURB;

        // Token goes here
        JDABuilder.createDefault("",
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_WEBHOOKS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MEMBERS
        )
                // Need to add intents later
                .setStatus(status)
                .addEventListeners(new Listener())
                .build();
    }
    public static void main(String[] args) throws LoginException, SQLException {
        new Bot();
    }
}
