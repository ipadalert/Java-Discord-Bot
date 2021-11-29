package File.Commands.admin;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Prefix;
import File.database.SQLiteDataSource;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

// Prefix changer for guild specific prefixes.
public class SetPrefixCommand implements ICommand {
    public void handle(CommandsContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        Member member = ctx.getMember();
        if(!member.hasPermission(Permission.ADMINISTRATOR)){
            return;
        }
        if(args.isEmpty()){
            channel.sendMessage("Missing args").queue();
            return;
        }
        String newPrefix = String.join("",args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);
        channel.sendMessage("Prefix set to: `" + newPrefix+"`").queue();
    }

    public String getName() {
        return "prefix";
    }

    public String getHelp() {
        return "";
    }

    // Uses SQL for db lang, updates prefix based on user input
    private void updatePrefix(long guildId, String newPrefix){
        Prefix.PREFIXES.put(guildId, newPrefix);
        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {
            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
