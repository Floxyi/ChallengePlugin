package de.floxyii.challengeplugin.challenge.challenges;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class NoJumpChallenge implements Listener, Challenge {

    @Override
    public String getName() {
        return "NoJump";
    }

    public void activateChallenge() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(getPrefix() + ChatColor.GREEN + getName() + "-Challenge got activated!");
        }
        addListeners();
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
        return ChatColor.RED + "No Jumps allowed. You failed!";
    }

    @Override
    public void stopChallenge() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void resumeChallenge() {
        addListeners();
    }

    @Override
    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    public void onPlayerMove(PlayerJumpEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if(event.getTo().getY() - event.getFrom().getY() > 0.35) {
                event.setCancelled(true);
                ChallengePlugin.getChallengeManager().endChallenge();
            }
        }
    }
}
