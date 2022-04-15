package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class HalfHeartChallenge implements Listener, Challenge {

    boolean isActive = false;
    @Override
    public String getName() {
        return "HalfHeart";
    }

    public void activateChallenge() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(getPrefix() + ChatColor.GREEN + getName() + "-Challenge got activated!");
            player.setMaxHealth(1);
        }
        addListeners();
        isActive = true;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "In this challenge you only have half a heart. Spend it wisely or don't spend it at all!";
    }

    @Override
    public String getDeathMessage() {
        return ChatColor.RED + "Someone lost all of its hearts. You failed!";
    }

    @Override
    public void saveContents(String path) {}

    @Override
    public void loadContents(String path) {}

    @Override
    public void stopChallenge() {
        HandlerList.unregisterAll(this);
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setMaxHealth(20);
            player.setHealth(20);
            player.sendMessage(getPrefix() + ChatColor.RED + getName() + "-Challenge got deactivated!");
        }
        isActive = false;
    }

    @Override
    public void resumeChallenge() {
        addListeners();
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setMaxHealth(1);
        }
        isActive = true;
    }

    @Override
    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            event.setCancelled(true);
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(getPrefix() + event.getPlayer().getName() + " got damage from " + event.getPlayer().getLastDamageCause().getCause() + ChatColor.RED + " ❤" + event.getPlayer().getLastDamageCause().getDamage());
            }
            ChallengePlugin.getChallengeManager().endChallenge();
        }
    }
}
