package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import net.dv8tion.jda.api.JDA;

// Not used, here for testing
public class PingCommand implements ICommand {
    @Override
    public void handle(final CommandsContext ctx) {
        final JDA jda = ctx.getJDA();
        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessageFormat("Rest ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue()
        );
    }
    @Override

    public String getHelp() {
        return "Shows bots ping to discord servers";
    }
    @Override

    public String getName() {
        return "ping";
    }
}
