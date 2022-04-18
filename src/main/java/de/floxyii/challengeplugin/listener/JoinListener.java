package de.floxyii.challengeplugin.listener;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import de.floxyii.challengeplugin.challenge.modules.Module;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setPlayerListHeader(ChatColor.GOLD + "-= Challenge Server =-");
        player.setPlayerListFooter(ChatColor.GREEN + "made by Floxyii with " + ChatColor.RED + "❤");

        if(ChallengePlugin.getChallengeManager().getRunningChallenge() != null) {
            if(ChallengePlugin.getChallengeManager().getRunningChallenge().isActive()) {
                player.sendMessage(ChallengePlugin.getPrefix() + "Challenge running: " + ChatColor.GOLD + ChallengePlugin.getChallengeManager().getRunningChallenge().getName());
            } else {
                player.sendMessage(ChallengePlugin.getPrefix() + "Challenge stopped: " + ChatColor.GOLD + ChallengePlugin.getChallengeManager().getRunningChallenge().getName());
            }
        }

        if(!ChallengePlugin.getChallengeManager().getActiveModules().isEmpty()) {
            for(Module module : ChallengePlugin.getChallengeManager().getActiveModules()) {
                player.sendMessage(ChallengePlugin.getPrefix() + "Module active: " + ChatColor.GOLD + module.getName());
            }
        }

        if(ChallengePlugin.getChallengeTimer().getTime() != 0) {
            if(ChallengePlugin.getChallengeTimer().isActive()) {
                player.sendMessage(ChallengePlugin.getPrefix() + "Timer running: " + ChatColor.GOLD + ChallengePlugin.getChallengeTimer().getFormattedTime());
            } else {
                player.sendMessage(ChallengePlugin.getPrefix() + "Timer stopped: " + ChatColor.GOLD + ChallengePlugin.getChallengeTimer().getFormattedTime());
            }
        }

        ChallengePlugin.getChallengeTimer().addPlayer(player);
    }
}
