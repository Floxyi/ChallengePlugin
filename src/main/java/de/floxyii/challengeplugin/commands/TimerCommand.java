package de.floxyii.challengeplugin.commands;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TimerCommand implements TabExecutor {

    Timer timer = ChallengePlugin.getChallengeTimer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("start")) {
                    if(timer.startTimer()) {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Timer started!");
                    } else {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "No timer found or already started!");
                    }
                } else if(args[0].equalsIgnoreCase("stop")) {
                    if(timer.stopTimer()) {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Timer stopped!");
                    } else {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "No timer found or already stopped!");
                    }
                } else if(args[0].equalsIgnoreCase("reset")) {
                    if(timer.resetTimer()) {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Timer reset!");
                    } else {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "No timer found!");
                    }
                } else if(args[0].equalsIgnoreCase("remove")) {
                    if(timer.removePlayer(player)) {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Timer removed!");
                    } else {
                        player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "No timer found!");
                    }
                } else {
                    player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "You have to enter /timer <start> <stop> <reset> <remove>!");
                }
            } else {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "You have to enter /timer <start> <stop> <reset> <remove>!");
            }
        } else {
            sender.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "You have to be a Player!");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        commands.add("start");
        commands.add("stop");
        commands.add("reset");
        commands.add("remove");
        return commands;
    }
}
