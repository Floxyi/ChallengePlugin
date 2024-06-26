package de.floxyii.challengeplugin.commands;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.utils.Challenge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChallengeCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "You need to be a player!");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args.length > 2 || (args.length == 2 && args[0].equals("stop")) || (args.length == 2 && args[0].equals("remove")) || (args.length == 1 && args[0].equals("info"))) {
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Failed! Please use " +
                    ChatColor.GOLD + "/challenge <start \"challenge name\"> <stop> <resume> <remove> <info \"challenge name\">" + ChatColor.RED + "!");
            return false;
        }

        if (args[0].equalsIgnoreCase("start")) {

            if (args.length == 1) {
                player.openInventory(ChallengePlugin.getChallengeManager().getChallengeInventory());
                return false;
            }

            Challenge challengeFromArg = ChallengePlugin.getChallengeManager().getChallenge(args[1]);

            if (challengeFromArg == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Challenge " + ChatColor.GOLD + args[1] + ChatColor.RED + " wasn't found!");
                return false;
            }

            if (ChallengePlugin.getChallengeManager().getRunningChallenge() == null) {
                ChallengePlugin.getChallengeManager().startChallenge(challengeFromArg);
                return true;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "The challenge " + ChatColor.GOLD + ChallengePlugin.getChallengeManager().getRunningChallenge().getName() + ChatColor.RED + " is already running!");
            return false;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            Challenge challenge = ChallengePlugin.getChallengeManager().getRunningChallenge();

            if (challenge != null) {
                ChallengePlugin.getChallengeManager().stopChallenge();
                return true;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "There is no running challenge!");
            return false;
        }

        if (args[0].equalsIgnoreCase("resume")) {
            if (ChallengePlugin.getChallengeManager().resumeChallenge()) {
                return true;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "There is no stopped challenge!");
            return false;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            Challenge challenge = ChallengePlugin.getChallengeManager().getRunningChallenge();

            if (challenge != null) {
                ChallengePlugin.getChallengeManager().removeChallenge();
                return true;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "There is no running challenge to be removed!");
            return false;
        }

        if (args[0].equalsIgnoreCase("info")) {
            Challenge challenge = ChallengePlugin.getChallengeManager().getChallenge(args[1]);

            if (challenge == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Challenge wasn't found!");
                return false;
            }

            player.sendMessage(challenge.getPrefix() + challenge.getDescription());
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> commands = new ArrayList<>();
            commands.add("start");
            commands.add("stop");
            commands.add("resume");
            commands.add("info");
            commands.add("remove");
            return commands;
        }

        if (args.length == 2 && !(args[0].equals("stop")) && !(args[0].equals("resume")) && !(args[0].equals("remove"))) {
            return ChallengePlugin.getChallengeManager().getChallengeNames();
        }
        return new ArrayList<>();
    }
}
