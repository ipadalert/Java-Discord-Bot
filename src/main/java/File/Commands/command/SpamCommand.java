package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import net.dv8tion.jda.api.entities.Member;


public class SpamCommand implements ICommand {
    public String getName() {
        return "spam";
    }

    public void handle(CommandsContext ctx) {

        Member me = ctx.getMember();
        StringBuilder after = new StringBuilder();
        for(int k = 0; k < ctx.getArgs().size()-1; k++){
            after.append(ctx.getArgs().get(k + 1)).append(" ");
        }
        if(me.getId().equalsIgnoreCase("")){
            ctx.getMessage().delete().complete();
            for(int i = 0; i < Integer.parseInt(ctx.getArgs().get(0)); i++){
                ctx.getChannel().sendMessage(after.toString()).queue();
            }
        }
        System.out.println("-Spam command ran by " + ctx.getMember().getUser().getAsTag() +" in the " + ctx.getGuild().getName() + " guild");
    }

    public String getHelp() {
        return "";
    }
}
