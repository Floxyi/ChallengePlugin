package de.floxyii.challengeplugin.commands;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.utils.Waypoint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WaypointCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "You need to be a player!");
            return false;
        }

        Player player = (Player) sender;

        if(args.length != 2) {
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Failed! Please use " +
                    ChatColor.GOLD + "/waypoint <create \"waypoint name\"> <show \"waypoint name\"> <delete \"waypoint name\">" + ChatColor.RED + "!");
            return false;
        }

        if(args[0].equalsIgnoreCase("create")) {
            String name = args[1];

            for(Waypoint waypoint : ChallengePlugin.getChallengeManager().waypoints) {
                if(waypoint.name.equals(name)) {
                    player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "A waypoint with the name " + ChatColor.GOLD + name + ChatColor.RED + " already exists!");
                    return false;
                }
            }

            ChallengePlugin.getChallengeManager().waypoints.add(new Waypoint(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), name));

            Waypoint createdWaypoint = null;
            for(Waypoint waypoint : ChallengePlugin.getChallengeManager().waypoints) {
                if(waypoint.name.equals(name)) {
                    createdWaypoint = waypoint;
                }
            }

            if(createdWaypoint == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Waypoint could not be created!");
                return false;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Successfully created Waypoint! [" + ChatColor.GOLD + createdWaypoint.name + ChatColor.GREEN + "]");
            return true;
        }

        if(args[0].equalsIgnoreCase("show")) {

            if(args[1].equals("")) {
                for(Waypoint waypoint : ChallengePlugin.getChallengeManager().waypoints) {
                    player.sendMessage(ChallengePlugin.getPrefix() + waypoint.getCoordinates());
                }
                return true;
            }

            String waypointName = args[1];

            Waypoint searchedWaypoint = null;
            for(Waypoint waypoint : ChallengePlugin.getChallengeManager().waypoints) {
                if(waypoint.name.equals(waypointName)) {
                    searchedWaypoint = waypoint;
                }
            }

            if(searchedWaypoint == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Waypoint with name " + ChatColor.GOLD + waypointName + ChatColor.RED + " does not exist!");
                return false;
            }

            player.sendMessage(ChallengePlugin.getPrefix() + searchedWaypoint.getCoordinates());
            return true;
        }

        if(args[0].equalsIgnoreCase("delete")) {
            String waypointName = args[1];

            Waypoint searchedWaypoint = null;
            for(Waypoint waypoint : ChallengePlugin.getChallengeManager().waypoints) {
                if(waypoint.name.equals(waypointName)) {
                    searchedWaypoint = waypoint;
                }
            }

            if(searchedWaypoint == null) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "Waypoint with name " + ChatColor.GOLD + waypointName + ChatColor.RED + " does not exist!");
                return false;
            }

            ChallengePlugin.getChallengeManager().waypoints.remove(searchedWaypoint);
            player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + "Successfully deleted Waypoint! [" + ChatColor.GOLD + searchedWaypoint.name + ChatColor.GREEN + "]");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 1) {
            List<String> commands = new ArrayList<>();
            commands.add("create");
            commands.add("show");
            commands.add("delete");
            return commands;
        }

        if(args.length == 2 && (!args[0].equalsIgnoreCase("create"))) {
            List<String> waypointNames = new ArrayList<>();
            for(Waypoint waypoint : ChallengePlugin.getChallengeManager().waypoints) {
                waypointNames.add(waypoint.name);
            }
            return waypointNames;
        }

        return new ArrayList<>();
    }
}
