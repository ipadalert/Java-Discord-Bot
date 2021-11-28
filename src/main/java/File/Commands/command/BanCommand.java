package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.List;

public class BanCommand implements ICommand {
    public void handle(final CommandsContext ctx) {
        Message message = ctx.getMessage();
        Member member = ctx.getMember();
        List<String> args = ctx.getArgs();
        if(!member.hasPermission(Permission.ADMINISTRATOR)){
            return;
        }
        if(args.size() <1 || message.getMentionedMembers().isEmpty()){
            ctx.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage("Enter a mentionable person").queue());
            return;
        }
        final Member target = message.getMentionedMembers().get(0);
        message.delete().queue();
        try {
            ctx.getGuild()
                    .ban(target,0)
                    .queue((__) -> ctx.getAuthor().openPrivateChannel().queue((channel)-> channel.sendMessage("Banned " + target.getEffectiveName()).queue()));
            System.out.println("-ban command ran by " + ctx.getMember().getUser().getAsTag() +" (banning " + target.getUser().getAsTag() + "from the " + ctx.getGuild().getName() + " guild)");
        }
        catch (HierarchyException e){
            ctx.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Too high roles").queue()
            );
        }
    }
    public String getHelp() {
        return "";
    }

    public String getName() {
        return "ban";
    }
}