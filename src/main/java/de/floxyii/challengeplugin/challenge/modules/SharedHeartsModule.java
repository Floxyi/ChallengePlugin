package de.floxyii.challengeplugin.challenge.modules;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SharedHeartsModule implements Module, Listener {

    boolean isActive = false;

    @Override
    public String getName() {
        return "Shared-Hearts";
    }

    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "If you take damage, all of the other player will do so too!";
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
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(isActive) {
            if(!(event.getEntity() instanceof Player)) {
                return;
            }

            Player damagedPlayer = (Player) event.getEntity();
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(getPrefix() + damagedPlayer.getName() + " got damage from " + event.getCause() + ChatColor.RED + " ❤" + event.getDamage() / 2);
                player.setHealth(player.getHealth() - event.getDamage());
            }
        }
    }
}
