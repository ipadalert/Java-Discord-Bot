package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Prefix;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AvatarCommand implements ICommand {
    public String getHelp() {
        return "";
    }

    public void handle(CommandsContext ctx) {
        final String prefix = Prefix.PREFIXES.get(ctx.getGuild().getIdLong());
        String name = ctx.getMessage().getContentRaw();
        Message message = ctx.getMessage();
        List<String> args = ctx.getArgs();
        if(message.getMentionedMembers().size()==0 && args.size() > 0) {
            if (name.startsWith(".av")){
                ctx.getChannel().sendMessage("Usage: `" + prefix + "av [mentionable member]`").queue();
            }
            else{
                ctx.getChannel().sendMessage("Usage: `" + prefix + "avatar [mentionable member]`").queue();
            }
            return;
        }
        String person = ctx.getMember().getEffectiveName();
        String url = ctx.getMember().getUser().getEffectiveAvatarUrl();
        if(!ctx.getMessage().getMentionedMembers().isEmpty()) {
            person = ctx.getMessage().getMentionedMembers().get(0).getEffectiveName();
            url = ctx.getMessage().getMentionedMembers().get(0).getUser().getAvatarUrl();
        }
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(new Color(255,255,255))
                .setDescription("**"+person + "'s avatar**")
                .setImage(url);
        ctx.getChannel().sendMessage(embed.build()).queue();
        System.out.println("-Avatar command ran by " + ctx.getMember().getUser().getAsTag() +" in the "+ctx.getGuild().getName() + " guild");
    }

    public String getName() {
        return "avatar";
    }

    public List<String> getAliases() {
        List<String> names = new ArrayList<String>();
        names.add("av");
        return names;
    }
}
