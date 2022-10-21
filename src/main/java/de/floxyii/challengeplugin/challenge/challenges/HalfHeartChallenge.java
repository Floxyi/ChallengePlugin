package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

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
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(1);
        }

        registerListener();
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
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
            player.setHealth(20);
            player.sendMessage(getPrefix() + ChatColor.RED + getName() + "-Challenge got deactivated!");
        }

        isActive = false;
    }

    @Override
    public void resumeChallenge() {
        registerListener();

        for(Player player : Bukkit.getOnlinePlayers()) {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(1);
        }

        isActive = true;
    }

    @Override
    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        if(player == null) {
            return;
        }

        if(!(player.getGameMode().equals(GameMode.SURVIVAL))) {
            return;
        }

        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(getPrefix() + player.getName() +
                    " got damage by " + ChatColor.GREEN +
                    Objects.requireNonNull(player.getLastDamageCause()).getCause() +
                    ChatColor.RED + " ❤" + player.getLastDamageCause().getDamage() / 2);
        }
    }
}
