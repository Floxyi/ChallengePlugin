package de.floxyii.challengeplugin.commands;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.modules.Module;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ModuleCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "You need to be a player!");
            return false;
        }

        Player player = (Player) sender;

        if(args.length < 1 || args.length > 3 || (args.length != 3 && args[0].equals("edit")) || (args.length > 2 && args[0].equals("show")) || (args.length != 2 && args[0].equals("info"))) {
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Failed! Please use " +
                    ChatColor.GOLD + "/module <edit \"module\" \"bool\"> <show \"module\"> <info \"module\"> <clear>" + ChatColor.RED + "!");
            return false;
        }

        if(args[0].equalsIgnoreCase("edit")) {
            Module module = ChallengePlugin.getChallengeManager().getModule(args[1]);

            if(!(args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("true"))) {
                return false;
            }

            boolean state = Boolean.parseBoolean(args[2]);

            if(module == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module" + ChatColor.GOLD + args[1] + ChatColor.RED + "wasn't found!");
                return false;
            }

            if(!module.isActive()) {
                if(state) {
                    player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module got deactivated");
                } else {
                    player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module got activated!");
                }
                ChallengePlugin.getChallengeManager().setModule(module, state);
                return true;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module " + ChatColor.GOLD + module.getName() + ChatColor.RED + "is already in that state or no challenge is already running!");
            return false;
        }

        if(args[0].equalsIgnoreCase("show")) {
            if(args.length == 2) {
                Module module = ChallengePlugin.getChallengeManager().getModule(args[1]);

                if(module == null) {
                    player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module " + ChatColor.GOLD + args[1] + ChatColor.RED + "wasn't found!");
                    return false;
                }

                if(module.isActive()) {
                    player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module (" + ChatColor.GREEN + "true" + ChatColor.RESET + "):");
                } else {
                    player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module (" + ChatColor.RED + "false" + ChatColor.RESET + "):");
                }
                player.sendMessage(ChallengePlugin.getPrefix() + module.getDescription());
                return true;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + "---------------------------");
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GOLD + "Overview:");
            for(Module module : ChallengePlugin.getChallengeManager().getModulesList()) {
                player.sendMessage("");
                if(module.isActive()) {
                    player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + module.getName() + ChatColor.RESET + "-Module (" + ChatColor.GREEN + "true" + ChatColor.RESET + "):");
                } else {
                    player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + module.getName() + ChatColor.RESET + "-Module (" + ChatColor.RED + "false" + ChatColor.RESET + "):");
                }
                player.sendMessage(ChallengePlugin.getPrefix() + module.getDescription());
            }
            player.sendMessage(ChallengePlugin.getPrefix() + "---------------------------");

        }

        if(args[0].equalsIgnoreCase("info")) {
            Module module = ChallengePlugin.getChallengeManager().getModule(args[1]);

            if(module == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module " + ChatColor.GOLD + args[1] + ChatColor.RED + "wasn't found!");
                return false;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + module.getDescription());
            return true;
        }

        if(args[0].equalsIgnoreCase("clear")) {
            if(ChallengePlugin.getChallengeManager().getActiveModules().isEmpty()) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "No modules currently active!");
                return false;
            }

            for(Module module : ChallengePlugin.getChallengeManager().getActiveModules()) {
                ChallengePlugin.getChallengeManager().setModule(module, false);
            }
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "All modules got deactivated!");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 1) {
            List<String> commands = new ArrayList<>();
            commands.add("edit");
            commands.add("show");
            commands.add("info");
            commands.add("clear");
            return commands;
        }

        if(args.length == 2 && (args[0].equals("edit"))) {
            return ChallengePlugin.getChallengeManager().getModuleNames();
        }

        if(args.length == 2 && (args[0].equals("show"))) {
            return ChallengePlugin.getChallengeManager().getModuleNames();
        }

        if(args.length == 2 && (args[0].equals("info"))) {
            return ChallengePlugin.getChallengeManager().getModuleNames();
        }

        if(args.length == 3 && (args[0].equals("edit"))) {
            List<String> commands = new ArrayList<>();
            commands.add("false");
            commands.add("true");
            return commands;
        }

        return new ArrayList<>();
    }
}
