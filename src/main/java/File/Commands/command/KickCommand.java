package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.List;

// Kicks people
public class KickCommand implements ICommand {
    public void handle(final CommandsContext ctx) {
        Message message = ctx.getMessage();
        Member member = ctx.getMember();
        List<String> args = ctx.getArgs();
        if(!member.hasPermission(Permission.KICK_MEMBERS)){
            return;
        }
        if(args.size() <1 || message.getMentionedMembers().isEmpty()){
            ctx.getAuthor().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage("Enter a name").queue();
                    }
            );
            return;
        }
        final Member target = message.getMentionedMembers().get(0);
        message.delete().queue();
        try {
            ctx.getGuild()
                    .kick(target)
                    .queue((__) -> ctx.getAuthor().openPrivateChannel().queue((channel)-> {
                        channel.sendMessage("Kicked " + target.getEffectiveName()).queue();
                    }));
            System.out.println("-Kick command ran by " + ctx.getMember().getUser().getAsTag() +" (kicking " + target.getUser().getAsTag() + "from the " + ctx.getGuild().getName() + " guild)");
        }
        catch (HierarchyException e){
            ctx.getAuthor().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage("Person is above me").queue();
                    }
            );
        }
    }
    public String getHelp() {
        return "";
    }

    public String getName() {
        return "kick";
    }
}
