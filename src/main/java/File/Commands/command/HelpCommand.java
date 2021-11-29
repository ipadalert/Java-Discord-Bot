package File.Commands.command;
import File.CommandManager;
import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Prefix;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    // Displays commands, help for more command information is not implemented
    // (Used for personal needs, not really inclusive to other users)
    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }
    @Override
    public void handle(CommandsContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            final String prefix = Prefix.PREFIXES.get(ctx.getGuild().getIdLong());
            builder.append("```");
            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append(prefix).append(it).append("\n")
            );
            builder.append("```");
            channel.sendMessage(builder.toString()).queue();
            System.out.println("-Help command ran by " + ctx.getMember().getUser().getAsTag() +" in the " + ctx.getGuild().getName() + " guild");
        }
        else{
            String search = args.get(0);
            ICommand command = manager.getCommand(search);
            channel.sendMessage(command.getHelp()).queue();
            if (command == null) {
                channel.sendMessage("Nothing found for " + search).queue();
                return;
            }
        }
    }
    @Override
    public String getName() {
        return "help";
    }
    @Override
    public String getHelp() {
        return "Shows the list with commands in the bot";
    }
}
