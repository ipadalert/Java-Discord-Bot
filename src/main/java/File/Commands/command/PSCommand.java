package File.Commands.command;

import File.Commands.CommandsContext;
import File.Commands.ICommand;

// Random print screen gen, mostly for fun and used as a game
public class PSCommand implements ICommand {
    public void handle(CommandsContext ctx) {
        ctx.getMessage().delete().queue();
        String [] low = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        String [] up = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String [] num ={"0","1","2","3","4","5","6","7","8","9"};
        StringBuilder mes = new StringBuilder();
        StringBuilder place = new StringBuilder("https://prnt.sc/");
        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < 6; i++) {
                int rand = (int) (Math.random() * (2));
                if (rand == 0) {
                    place.append(low[(int) (Math.random() * (26))]);
                }
                else if (rand == 1) {
                    place.append(num[(int) (Math.random() * (10))]);
                }
            }
            mes.append(place).append("\n");
            place = new StringBuilder("https://prnt.sc/");
        }

        ctx.getChannel().sendMessage(mes + " -Requested by "+ ctx.getMember().getEffectiveName()).queue();
        System.out.println("-Printscreen command ran by " + ctx.getMember().getUser().getAsTag() +" in the " + ctx.getGuild().getName() + " guild");
    }

    public String getName() {
        return "ps";
    }

    public String getHelp() {
        return "";
    }
}
