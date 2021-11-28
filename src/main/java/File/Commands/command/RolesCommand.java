package File.Commands.command;
import File.Commands.CommandsContext;
import File.Commands.ICommand;
import File.Prefix;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RolesCommand implements ICommand {
    private final ArrayList<String> rolesAdded = new ArrayList<>();
    private final ArrayList<String> rolesRemoved = new ArrayList<>();
    private final ArrayList<String> rolesDNE = new ArrayList<>();
    private final ArrayList<String> rolesAbove = new ArrayList<>();
    private final ArrayList<String> rolesCT = new ArrayList<>();
    private boolean send = false;
    public void handle(final CommandsContext ctx) {
        send = false;
        Message message = ctx.getMessage();
        String author = ctx.getAuthor().getId();
        Member member = ctx.getMember();
        List<String> args = ctx.getArgs();
        int place = 0;
        boolean checkdev = false;
        boolean isSilent = false;
        for(int i = 0; i < member.getRoles().size(); i++){
            if(member.getRoles().get(i).getName().equalsIgnoreCase("dev")){
                checkdev = true;
                i = member.getRoles().size();
            }
        }
        if (checkdev) {
            if (message.getMentionedMembers().size() == 0 && args.size() > 0) {
                catcher(false, ctx);
                return;
            }
            if (args.size() == 0) {
                StringBuilder builder = new StringBuilder();
                builder.append("```Roles: ");
                for (int i = 0; i < ctx.getGuild().getRoles().size(); i++) {
                    if(i != ctx.getGuild().getRoles().size()-1) {
                        builder.append(ctx.getGuild().getRoles().get(i).getName()).append(", ");
                    }
                    else {
                        builder.append(ctx.getGuild().getRoles().get(i).getName());
                    }
                }
                builder.append("```");
                ctx.getChannel().sendMessage(builder.toString()).queue();
                return;
            }
            if (args.get(0).equalsIgnoreCase("-s")) {
                isSilent = true;
                place = 1;
                message.delete().queue();
            }
            if (!ctx.getArgs().get(place).equalsIgnoreCase(starts(ctx, place) + ctx.getMessage().getMentionedMembers().get(0).getId() + ">")) {
                catcher(false, ctx);
                return;
            }
            returnMessage(isSilent, ctx.getMessage().getMentionedMembers().get(0).getEffectiveName(), ctx);
            clear();
        }
    }
    public String getHelp() {
        return "";
    }
    public String getName() {
        return "roles";
    }
    public void returnMessage(boolean silent, String userName, CommandsContext ctx){
        StringBuilder build = new StringBuilder("```");
        String member = ctx.getMember().getUser().getAsTag();
        String guild = ctx.getGuild().getName();
        build.append(role(userName, member, guild, ctx, silent));
        if (!send) {
            if (silent) {
                ctx.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(build.toString()).queue());
            } else {
                ctx.getChannel().sendMessage(build.toString()).queue();
            }
        }
    }
    public String role(String name, String member, String guild, CommandsContext ctx, boolean silent){
        String message = "";
        ArrayList<String> after = new ArrayList<>();
        StringBuilder roles = new StringBuilder();
        Member target = ctx.getMessage().getMentionedMembers().get(0);
        List<String> args = ctx.getArgs();
        StringBuilder temp = new StringBuilder();
        String adder = "";
        int holder =1;
        if(silent){
            holder =2;
            adder = " silently";
        }
        for (int i = holder; i < args.size(); i++) {
            if (i < args.size() - 1) {
                temp.append(args.get(i)).append(" ");
            } else {
                temp.append(args.get(i));
            }
        }
        if(temp.toString().startsWith(" ")){
            temp = new StringBuilder(temp.toString().replaceFirst(" ", ""));
        }
        addRemover(ctx, temp, target, after, silent);
        if (rolesRemoved.size() >0) {
            message += "Role" + local(rolesRemoved) + finalRoles(rolesRemoved, roles) + " removed from " + name + "\n";
            System.out.println("-Role command ran by " + member + " removing role" + local(rolesRemoved) + finalRoles(rolesRemoved, roles) + " from " + target.getUser().getAsTag() + " within the " + guild + " guild" + adder);
        }
        if(rolesAdded.size() > 0){
            message += "Role" + local(rolesAdded) + finalRoles(rolesAdded, roles) + " added to " + name + "\n";
            System.out.println("-Role command ran by " + member + " adding role" + local(rolesAdded) + finalRoles(rolesAdded, roles) + " to " + target.getUser().getAsTag() + " within the " + guild + " guild"  + adder);
        }
        if(rolesAbove.size() > 0) {
            String plural = " are ";
            if(rolesAbove.size() == 1){
                plural = " is ";
            }
            message += "Role" + local(rolesAbove) + finalRoles(rolesAbove, roles) + plural +"above me\n";
        }
        if(rolesDNE.size() > 0) {
            String plural = " do ";
            if(rolesDNE.size() == 1){
                plural = " does ";
            }
            message += "Role" + local(rolesDNE) + finalRoles(rolesDNE, roles) + plural + "not exist\n";
        }
        if(rolesCT.size() > 0){
            message += "Role" + local(rolesCT) + finalRoles(rolesCT, roles) + " cannot be added or removed";
        }
        return message +"```";
    }
    public boolean hasRole(Member target, CommandsContext ctx, String fm, boolean silent){
        boolean hasRole = false;
        for (int k = 0; k < target.getRoles().size(); k++) {
            if (target.getRoles().get(k).equals(ctx.getGuild().getRolesByName(fm, true).get(0))) {
                k = target.getRoles().size();
                hasRole = true;
            }
        }
        return hasRole;
    }
    public void addRemover(CommandsContext ctx, StringBuilder temp, Member target, ArrayList<String> after, boolean silent){
        if(hasComma(temp)){
            String[] temp2 = temp.toString().split(", ");
            after.addAll(Arrays.asList(temp2));
            for (String fm : after) {
                try {
                    extensionAR(ctx, target, fm, silent);
                }
                catch (ErrorResponseException ignored){}
            }
        }
        else{
            extensionAR(ctx, target, temp.toString(), silent);
        }
    }
    public void extensionAR(CommandsContext ctx, Member target, String fm, boolean silent){
        try {
            if(ctx.getGuild().getRolesByName(fm, true).get(0).isManaged() || ctx.getGuild().getRolesByName(fm, true).get(0).isPublicRole()){
                rolesCT.add(fm);
            }
            else {
                if (hasRole(target, ctx, fm, silent)) {
                    ctx.getGuild().removeRoleFromMember(target, ctx.getGuild().getRolesByName(fm, true).get(0)).queue();
                    rolesRemoved.add(fm);
                }
                if (!hasRole(target, ctx, fm, silent)) {
                    ctx.getGuild().addRoleToMember(target, ctx.getGuild().getRolesByName(fm, true).get(0)).queue();
                    rolesAdded.add(fm);
                }
            }
        }
        catch (IndexOutOfBoundsException e){
            rolesDNE.add(fm);
        }
        catch (HierarchyException e){
            rolesAbove.add(fm);
        }
        catch (IllegalArgumentException e){
            catcher(silent, ctx);
            send = true;
        }
    }
    public void clear(){
        rolesRemoved.clear();
        rolesAdded.clear();
        rolesDNE.clear();
        rolesAbove.clear();
        rolesCT.clear();
    }
    public boolean hasComma(StringBuilder temp){
        boolean comma = false;
        if(temp.toString().contains(", ")){
            comma = true;
        }
        return comma;
    }
    public String finalRoles(ArrayList<String> ar, StringBuilder rolesOld){
        StringBuilder roles = new StringBuilder();
        for(int i = 0; i < ar.size(); i++) {
            if(ar.size()-1 != i) {
                roles.append("\"").append(ar.get(i)).append("\", ");
            }
            else {
                roles.append("\"").append(ar.get(i)).append("\"");
            }
        }
        return roles.toString();
    }
    public void catcher(boolean silent, CommandsContext ctx){
        final String prefix = Prefix.PREFIXES.get(ctx.getGuild().getIdLong());
        ctx.getChannel().sendMessage("Usage: `"+ prefix+ "roles [member] [role], [optional role]`").queue((nm) -> {
            nm.delete().queueAfter(10, TimeUnit.SECONDS);
            if(!silent) {
                ctx.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
            }
        });
    }
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
    public String local(ArrayList<String> local){
        String message = " ";
        if(local.size() != 1){
            message = "s: ";
        }
        return message;
    }
}