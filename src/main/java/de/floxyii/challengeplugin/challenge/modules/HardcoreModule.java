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

    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
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
                player.sendMessage(getPrefix() + ChatColor.GREEN + getName() + "-Module activated!");
            }
            addListeners();
        }
        if(!bool) {
            if(!isActive) {
                return false;
            }
            isActive = false;
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(getPrefix() + ChatColor.RED + getName() + "-Module deactivated!");
            }
            HandlerList.unregisterAll(this);
        }
        return true;
    }

    @Override
    public boolean isActive() {
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
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(getPrefix() + event.getPlayer().getName() + " got damage from " + event.getPlayer().getLastDamageCause().getCause() + ChatColor.RED + " ❤" + event.getPlayer().getLastDamageCause().getDamage());
                }
                ChallengePlugin.getChallengeManager().endChallenge();
            }
        }
    }

}
