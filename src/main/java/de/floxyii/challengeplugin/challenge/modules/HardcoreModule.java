package de.floxyii.challengeplugin.challenge.modules;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class HardcoreModule implements Module, Listener {

    boolean isActive = false;

    @Override
    public String getName() {
        return "Hardcore";
    }

    @Override
    public String getDescription() {
        return "If you die once, the challenge is over!";
    }

    @Override
    public boolean setActive(boolean bool) {
        if(bool) {
            if(isActive) {
                return false;
            }
            isActive = true;
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GREEN + getName() + "-Module activated!");
            }
            addListeners();
        }
        if(!bool) {
            if(!isActive) {
                return false;
            }
            isActive = false;
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + getName() + "-Module deactivated!");
            }
            HandlerList.unregisterAll(this);
        }
        return true;
    }

    @Override
    public boolean getActive() {
        return isActive;
    }

    private void addListeners() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        if(isActive) {
            if(event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                event.setCancelled(true);
                ChallengePlugin.getChallengeManager().endChallenge();
            }
        }
    }

}
