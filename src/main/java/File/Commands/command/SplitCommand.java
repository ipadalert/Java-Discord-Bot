package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import net.dv8tion.jda.api.entities.Member;

// Splits words up by spaces and outputs them letter by letter
public class SplitCommand implements ICommand {
    public String getHelp() {
        return "";
    }

    public String getName() {
        return "split";
    }

    public void handle(CommandsContext ctx) {
        Member me = ctx.getMember();
        if(me.getId().equalsIgnoreCase("")){
            ctx.getMessage().delete().complete();
            for(int i = 0; i < ctx.getArgs().size(); i++){
                if(!ctx.getArgs().get(i).equalsIgnoreCase("")){
                    ctx.getChannel().sendMessage(ctx.getArgs().get(i)).queue();
                }
            }
        }
        System.out.println("-Split command ran by " + ctx.getMember().getUser().getAsTag() +" in the " + ctx.getGuild().getName() + " guild");
    }
}
