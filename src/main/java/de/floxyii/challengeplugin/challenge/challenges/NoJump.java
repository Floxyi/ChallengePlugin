package de.floxyii.challengeplugin.challenge.challenges;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class NoJump implements Listener, Challenge {

    @Override
    public String getName() {
        return "NoJump";
    }

    public void onActivate() {
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
            event.setCancelled(true);
            ChallengePlugin.getChallengeManager().endChallenge();
        }
    }
}
