package File.Commands;

import java.util.List;

public interface ICommand {
    void handle(CommandsContext ctx);
    String getName();
    String getHelp();
    default List<String> getAliases(){
        return List.of();
    }
}
