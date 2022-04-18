package de.floxyii.challengeplugin.challenge.modules;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HalfHeartModule implements Module, Listener {

    boolean isActive = false;

    @Override
    public String getName() {
        return "HalfHeart";
    }

    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "You only have one heart!";
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
                player.setMaxHealth(1);
            }
        }
        if(!bool) {
            if(!isActive) {
                return false;
            }
            isActive = false;
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(getPrefix() + ChatColor.RED + getName() + "-Module deactivated!");
                player.setMaxHealth(20);
                player.setHealth(20);
            }
        }
        return true;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setMaxHealth(1);
    }

}
