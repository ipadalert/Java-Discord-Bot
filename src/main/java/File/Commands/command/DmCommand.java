package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Prefix;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class DmCommand implements ICommand {
    public void handle(final CommandsContext ctx)  {
        final String prefix = Prefix.PREFIXES.get(ctx.getGuild().getIdLong());
        if (ctx.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ctx.getMessage().delete().queue();
            String fullmessage = ctx.getMessage().getContentRaw().replaceFirst(prefix + "dm ", "");
            if (ctx.getArgs().isEmpty()) {
                User user = ctx.getAuthor();
                user.openPrivateChannel().queue((channel) -> {
                    channel.sendMessage("Enter a message").queue();
                });
            }
            else {
                int place = 0;
                StringBuilder build = new StringBuilder("**These users did not receive the mass dm:**\n");
                for (int i = 0; i < ctx.getGuild().getMembers().size(); i++) {
                    User user = ctx.getGuild().getMembers().get(i).getUser();
                    if (!user.isBot()) {
                        try {
                            user.openPrivateChannel().complete().sendMessage(fullmessage + "**\n\n-Message sent by `" + ctx.getMember().getUser().getAsTag() + "` from the `" + ctx.getGuild().getName() + "` guild**").complete();
                        }
                        catch (ErrorResponseException e){
                            place++;
                            build.append(user.getAsTag()).append("\n");
                        }
                    }
                }
                if(place != 0) {
                    ctx.getMember().getUser().openPrivateChannel().queue(channel -> {
                        channel.sendMessage(build.toString()).queue();
                    });
                }
                System.out.println("-Dm command ran by " + ctx.getMember().getUser().getAsTag() +" (dming all members from the " + ctx.getGuild().getName() + " guild)\nMessage sent:\n"+fullmessage);
            }
        }
    }
    public String getName() {
        return "dm";
    }

    public String getHelp() {
        return "";
    }
}
