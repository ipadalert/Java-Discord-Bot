package File;

import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Commands.admin.SetPrefixCommand;
import File.Commands.command.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    public CommandManager(){
        addCommand(new HelpCommand(this));
        addCommand(new KickCommand());
        addCommand(new BanCommand());
        addCommand(new PurgeCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new DmCommand());
        addCommand(new RolesCommand());
        addCommand(new AvatarCommand());
        addCommand(new SpamCommand());
        addCommand(new SplitCommand());
        addCommand(new PSCommand());
        addCommand(new NitroCommand());
        addCommand(new InfoCommand());
        addCommand(new MessageCommand());
    }
    private void addCommand(ICommand cmd){
        boolean nameFound = this.commands.stream().anyMatch((it)-> it.getName().equalsIgnoreCase(cmd.getName()));
        if(nameFound){
            throw new IllegalArgumentException("A command with this name is already present");
        }
        commands.add(cmd);
    }
    public List<ICommand> getCommands(){
        return commands;
    }
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();
        for(ICommand cmd:this.commands){
            if(cmd.getName().equals(searchLower)||cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }
        return null;
    }
    void handle(GuildMessageReceivedEvent event, String prefix){
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)"+ Pattern.quote(prefix),"")
                .split(" ");
        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);
        if(cmd!= null){
            List<String> args = Arrays.asList(split).subList(1,split.length);
            CommandsContext ctx = new CommandsContext(event, args);
            cmd.handle(ctx);
        }
    }
}

