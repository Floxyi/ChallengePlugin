package de.floxyii.challengeplugin.commands;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.modules.Module;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModuleCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if(args.length < 1 || args.length > 3 || (args.length != 3 && args[0].equals("edit")) || (args.length > 2 && args[0].equals("show")) || (args.length != 2 && args[0].equals("info"))) {
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Failed! Please use " +
                    ChatColor.GOLD + "/module <edit \"module\" \"bool\"> <show \"module\"> <info \"module\">" + ChatColor.RED + "!");
            return false;
        }

        if(args[0].equalsIgnoreCase("edit")) {
            Module module = ChallengePlugin.getChallengeManager().getModule(args[1]);
            boolean state = Boolean.parseBoolean(args[2]);

            if(module == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module wasn't found!");
                return false;
            }

            if(ChallengePlugin.getChallengeManager().setModule(module, state)) {
                return true;
            } else {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module is already in that state or no challenge is already running!");
                return false;
            }
        }

        if(args[0].equalsIgnoreCase("show")) {
            if(args.length == 2) {
                Module module = ChallengePlugin.getChallengeManager().getModule(args[1]);

                if(module == null) {
                    player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module wasn't found!");
                    return false;
                }

                if(module.getActive()) {
                    player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module (" + ChatColor.GREEN + "true" + ChatColor.RESET + "):");
                }
                player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module (" + ChatColor.RED + "false" + ChatColor.RESET + "):");
                player.sendMessage(ChallengePlugin.getPrefix() + "  -" + module.getDescription());
                return true;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + "------------------------------------");
            player.sendMessage(ChallengePlugin.getPrefix() + "Overview:");
            for(Module module : ChallengePlugin.getChallengeManager().getModules()) {
                if(module.getActive()) {
                    player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module (" + ChatColor.GREEN + "true" + ChatColor.RESET + "):");
                }
                player.sendMessage(ChallengePlugin.getPrefix() + module.getName() + "-Module (" + ChatColor.RED + "false" + ChatColor.RESET + "):");
                player.sendMessage(ChallengePlugin.getPrefix() + "  -" + module.getDescription());
                player.sendMessage(ChallengePlugin.getPrefix() + "------------------------------------");
            }

        }

        if(args[0].equalsIgnoreCase("info")) {
            Module module = ChallengePlugin.getChallengeManager().getModule(args[1]);

            if(module == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Module wasn't found!");
                return false;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + module.getDescription());
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            List<String> commands = new ArrayList<>();
            commands.add("edit");
            commands.add("show");
            commands.add("info");
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
            commands.add("true");
            commands.add("false");
            return commands;
        }

        return new ArrayList<>();
    }
}
