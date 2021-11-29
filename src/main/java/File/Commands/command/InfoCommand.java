package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Prefix;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

// Displays info for users
public class InfoCommand implements ICommand {
    public void handle(final CommandsContext ctx) {
        ctx.getMessage().delete().queue();
        final String prefix = Prefix.PREFIXES.get(ctx.getGuild().getIdLong());
        if(ctx.getMessage().getMentionedMembers().isEmpty()||ctx.getMessage().getMentionedMembers().size()>1 || ctx.getArgs().get(0).isEmpty() || !(ctx.getArgs().get(0).equalsIgnoreCase(starts(ctx, 0)+ctx.getMessage().getMentionedMembers().get(0).getId()+">"))||ctx.getArgs().size()>1){
            ctx.getChannel().sendMessage("Usage: `"+prefix+"user [mentionable member]`").queue((message) -> {
                message.delete().queueAfter(10, TimeUnit.SECONDS);
            });
        }
        else{
            // Fancy formatting for reply
            DateTimeFormatter join = DateTimeFormatter.ofPattern("M-d-yyyy h:mm:ss a").withZone(ZoneId.of("America/New_York"));
            Member person = ctx.getMessage().getMentionedMembers().get(0);
            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(new Color(108,108,108)).setDescription("**"+person.getEffectiveName()+"'s Info**")
                    .addField("Account created on: ", person.getTimeCreated().format(join) + " EST",true)
                    .addField("Joined the server on: ", person.getTimeJoined().format(join)+ " EST", true)
                    .addField("User ID: ", person.getId(), true)
                    .setImage(ctx.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl())
                    .setFooter("Requested by -"+ ctx.getMember().getEffectiveName());
            ctx.getChannel().sendMessage(eb.build()).queue();
            System.out.println("-Info command ran by " + ctx.getMember().getUser().getAsTag() +" in the " + ctx.getGuild().getName() + " guild");
        }
    }

    public String getName() {
        return "user";
    }

    public String getHelp() {
        return "";
    }

    // Sloppy parameters, needs fixing
    public String starts(CommandsContext ctx, int place){
        String starts;
        if(ctx.getArgs().get(place).startsWith("<@!")){
            starts = "<@!";
        }
        else {
            starts = "<@";
        }
        return starts;
    }
}
