package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;

public class NitroCommand implements ICommand {
    public void handle(CommandsContext ctx) {
        ctx.getMessage().delete().queue();
        String[] low = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String[] up = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder place = new StringBuilder("https://discord.gift/");
        for (int k = 0; k < 5; k++) {
            for (int i = 0; i < 16; i++) {
                int rand = (int) (Math.random() * (3));
                if (rand == 0) {
                    place.append(low[(int) (Math.random() * (26))]);
                } else if (rand == 1) {
                    place.append(num[(int) (Math.random() * (10))]);
                } else if (rand == 2) {
                    place.append(up[(int) (Math.random() * (26))]);
                }
            }
            ctx.getChannel().sendMessage(place + " " + (k + 1)).queue();
            place = new StringBuilder("https://discord.gift/");
        }
        System.out.println("-Nitro gift command ran by " + ctx.getMember().getUser().getAsTag() + " in the " + ctx.getGuild().getName() + " guild");
    }

    public String getHelp() {
        return "";
    }

    public String getName() {
        return "dc";
    }
}
