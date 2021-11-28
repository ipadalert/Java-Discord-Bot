package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class PurgeCommand implements ICommand {
    public void handle(CommandsContext ctx) {
        TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        if(ctx.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (!args.isEmpty()) {
                try {
                    clear(channel, Integer.parseInt(args.get(0)) + 1);
                } catch (IllegalArgumentException ex) {
                    if (Integer.parseInt(args.get(0)) > 99) {
                        int high = Integer.parseInt(args.get(0)) / 100;
                        int mod = Integer.parseInt(args.get(0)) % 100;
                        int sum = 0;
                        for (int i = 0; i < high; i++) {
                            clear(ctx.getChannel(), 99);
                            sum++;
                        }
                        clear(ctx.getChannel(), mod + 1 + sum);
                    }
                }
                System.out.println("-"+ctx.getMember().getUser().getAsTag()+" purged " + ctx.getArgs().get(0) + " message(s) from the " + ctx.getChannel().getName() + " channel within the "+ ctx.getGuild().getName() + " guild");
            }
        }
    }
    public String getHelp() {
        return "Purges chat";
    }
    public String getName() {
        return "purge";
    }
    private void clear(TextChannel channel, int num){
        MessageHistory history = new MessageHistory(channel);
        List<Message> message;
        message = history.retrievePast(num).complete();
        channel.deleteMessages(message).queue();
    }
}
