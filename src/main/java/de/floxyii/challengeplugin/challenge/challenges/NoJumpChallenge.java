package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class NoJumpChallenge implements Listener, Challenge {

    boolean isActive = false;
    Player failedPlayer = null;

    @Override
    public String getName() {
        return "NoJump";
    }

    public void activateChallenge() {
        failedPlayer = null;

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(getPrefix() + ChatColor.GREEN + getName() + "-Challenge got activated!");
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
        return "In this challenge you are not allowed to jump! So be carefully going down a deep cave without the " +
                "needed equipment to get out again! Have a great time :) And please do not cheat by unbinding your jump key!";
    }

    @Override
    public String getDeathMessage() {
        return ChatColor.RED + failedPlayer.getName() + " has jumped! You failed.";
    }

    @Override
    public void saveContents(String path) {}

    @Override
    public void loadContents(String path) {}

    @Override
    public void stopChallenge() {
        HandlerList.unregisterAll(this);
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(getPrefix() + ChatColor.RED + getName() + "-Challenge got deactivated!");
        }
        isActive = false;
    }

    @Override
    public void resumeChallenge() {
        registerListener();
        isActive = true;
    }

    @Override
    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if(Objects.requireNonNull(event.getTo()).getY() - event.getFrom().getY() > 0.35) {
                failedPlayer = event.getPlayer();
                event.setCancelled(true);
                ChallengePlugin.getChallengeManager().endChallenge();
            }
        }
    }
}
