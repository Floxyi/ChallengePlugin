package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.Challenge;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class TheFloorIsLava implements Listener, Challenge {
    int height = -63;
    BukkitTask lavaUpdater;

    @Override
    public String getName() {
        return "TheFloorIsLava";
    }

    @Override
    public void onActivate() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChallengePlugin.getPrefix() + "The-Floor-is-Lava-Challenge got activated!");
        }
        updateLoadedChunks();
        updateLavaHeight();
        addListeners();
    }

    private void updateLoadedChunks() {
        for(Chunk chunk : ChallengePlugin.getChallengeWorld().getLoadedChunks()) {
            int chunkX = chunk.getX() * 16;
            int chunkZ = chunk.getZ() * 16;

            for (int x = 0; x <= 16; x++) {
                for (int z = 0; z <= 16; z++) {
                    for (int y = -63; y <= height; y++) {
                        chunk.getWorld().getBlockAt(chunkX + x, y, chunkZ + z).setType(Material.LAVA);
                    }
                }
            }
        }
    }

    private void updateLavaHeight() {
        lavaUpdater = new BukkitRunnable() {
            @Override
            public void run() {
                height++;
                updateLoadedChunks();
            }
        }.runTaskTimer(ChallengePlugin.getPlugin(), 300, 300);
    }

    @Override
    public void stopChallenge() {
        HandlerList.unregisterAll(this);
        lavaUpdater.cancel();
    }

    @Override
    public void resumeChallenge() {
        updateLavaHeight();
        addListeners();
    }

    @Override
    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    @Override
    public String getDeathMessage() {
        return ChatColor.RED + "The floor is lava!. You failed!";
    }

    @Override
    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(event.getEntity().getGameMode().equals(GameMode.SURVIVAL) && Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
            event.setCancelled(true);
            ChallengePlugin.getChallengeManager().endChallenge();
        }
    }
}
