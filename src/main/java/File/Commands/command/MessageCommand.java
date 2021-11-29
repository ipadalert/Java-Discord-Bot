package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Prefix;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

// Private messaging, used to bypass when user is blocked
// Needs fixing, quite sloppy
public class MessageCommand implements ICommand {
    public void handle(final CommandsContext ctx){
        final String prefix = Prefix.PREFIXES.get(ctx.getGuild().getIdLong());
        if(ctx.getMember().hasPermission(Permission.ADMINISTRATOR)){
            ctx.getMessage().delete().queue();
            if(ctx.getArgs().isEmpty() ||ctx.getArgs().size()<2){
                ctx.getChannel().sendMessage("Usage: `"+prefix+"msg [user id] [message]`").queue((message) -> {
                    message.delete().queueAfter(10, TimeUnit.SECONDS);
                });
            }
            else{
                try {
                    try {

                        String nm = ctx.getMessage().getContentRaw().replaceFirst(prefix + "msg " + ctx.getArgs().get(0) + " ", "");
                        try {
                            Objects.requireNonNull(ctx.getJDA().getUserById(ctx.getArgs().get(0))).openPrivateChannel().complete().sendMessage(nm + "\n\n-Sent by " + ctx.getMember().getUser().getAsTag()).complete();
                            ctx.getChannel().sendMessage("Message was successfully sent").queue((message) -> message.delete().queueAfter(2, TimeUnit.SECONDS));
                            System.out.println("-Message command ran by " + ctx.getMember().getUser().getAsTag() + ", sending \"" + nm + "\" to " + Objects.requireNonNull(ctx.getJDA().getUserById(ctx.getArgs().get(0))).getAsTag());
                        }
                        catch (ErrorResponseException e){
                            ctx.getChannel().sendMessage("Message was not received by user (likely blocked the bot or has messages turned off)").queue((message) -> message.delete().queueAfter(10, TimeUnit.SECONDS));
                        }
                    } catch (IllegalArgumentException e){
                        ctx.getChannel().sendMessage("Usage: `" + prefix + "msg [user id] [message]`").queue((message) -> message.delete().queueAfter(10, TimeUnit.SECONDS));
                    }
                }
                catch (NullPointerException e){
                    ctx.getChannel().sendMessage("Message was not received by user (likely does not share a guild with the bot)").queue((message) -> message.delete().queueAfter(10, TimeUnit.SECONDS));
                }
            }

        }
    }

    public String getName() {
        return "msg";
    }

    public String getHelp() {
        return null;
    }
}
