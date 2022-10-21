package de.floxyii.challengeplugin.challenge.modules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

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
                Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(1);
            }
        }
        if(!bool) {
            if(!isActive) {
                return false;
            }
            isActive = false;
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(getPrefix() + ChatColor.RED + getName() + "-Module deactivated!");
                Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
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
        Objects.requireNonNull(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(1);
    }

}
