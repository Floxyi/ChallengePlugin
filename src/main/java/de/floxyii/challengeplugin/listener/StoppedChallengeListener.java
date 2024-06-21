package de.floxyii.challengeplugin.listener;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class StoppedChallengeListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (ChallengePlugin.getChallengeManager().getRunningChallenge() != null && !ChallengePlugin.getChallengeManager().getRunningChallenge().isActive()) {
            Location from = event.getFrom();
            Location to = event.getTo();

            if (to != null && (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())) {
                Location newLocation = from.clone();
                newLocation.setPitch(to.getPitch());
                newLocation.setYaw(to.getYaw());
                event.setTo(newLocation);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if (ChallengePlugin.getChallengeManager().getRunningChallenge() != null && !ChallengePlugin.getChallengeManager().getRunningChallenge().isActive()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player) {
            if (ChallengePlugin.getChallengeManager().getRunningChallenge() != null && !ChallengePlugin.getChallengeManager().getRunningChallenge().isActive()) {
                event.setCancelled(true);
            }
        }
    }
}