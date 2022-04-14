package de.floxyii.challengeplugin.commands;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.challenges.Challenge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChallengeCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if(args.length == 0 || args.length > 2 || (args.length == 2 && args[0].equals("stop")) || (args.length == 1 && args[0].equals("start")) || (args.length == 1 && args[0].equals("info"))) {
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Failed! Please use " +
                    ChatColor.GOLD + "/challenge <start \"challenge name\"> <stop> <resume> <info \"challenge name\">" + ChatColor.RED + "!");
            return false;
        }

        if(args[0].equalsIgnoreCase("start")) {
            Challenge challenge = ChallengePlugin.getChallengeManager().getChallenge(args[1]);

            if(challenge == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Challenge wasn't found!");
                return false;
            }

            if(ChallengePlugin.getChallengeManager().startChallenge(challenge)) {
                return true;
            } else {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "A challenge is already running!");
                return false;
            }
        }

        if(args[0].equalsIgnoreCase("stop")) {
            if(ChallengePlugin.getChallengeManager().stopChallenge()) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Challenge got stopped!");
                return true;
            } else {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "There is no running challenge!");
                return false;
            }
        }

        if(args[0].equalsIgnoreCase("resume")) {
            if(ChallengePlugin.getChallengeManager().resumeChallenge()) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Challenge got resumed!");
                return true;
            } else {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "There is no running challenge!");
                return false;
            }
        }

        if(args[0].equalsIgnoreCase("info")) {
            Challenge challenge = ChallengePlugin.getChallengeManager().getChallenge(args[1]);

            if(challenge == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Challenge wasn't found!");
                return false;
            }

            player.sendMessage(challenge.getPrefix() + challenge.getDescription());
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            List<String> commands = new ArrayList<>();
            commands.add("start");
            commands.add("stop");
            commands.add("resume");
            commands.add("info");
            return commands;
        }

        if(args.length == 2 && !(args[0].equals("stop")) && !(args[0].equals("resume"))) {
            return ChallengePlugin.getChallengeManager().getChallengeNames();
        }
        return new ArrayList<>();
    }
}
