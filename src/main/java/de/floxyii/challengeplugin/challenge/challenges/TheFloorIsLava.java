package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.Challenge;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TheFloorIsLava implements Listener, Challenge {
    int height = 1;
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
        // prepareChunks();
        updateLavaHeight();
        addListeners();
    }

    private void prepareChunks() {
        for(Chunk chunk : ChallengePlugin.getChallengeWorld().getLoadedChunks()) {
            int chunkX = chunk.getX() * 16;
            int chunkZ = chunk.getZ() * 16;

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < height; y++) {
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
            }
        }.runTaskTimer(ChallengePlugin.getPlugin(), 20, 20);
    }

    @Override
    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @Override
    public void stopChallenge() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void resumeChallenge() {
        addListeners();
    }

    @EventHandler
    public void onChunkLoaded(ChunkLoadEvent event) {
        int chunkX = event.getChunk().getX() * 16;
        int chunkZ = event.getChunk().getZ() * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < height; y++) {
                    event.getChunk().getWorld().getBlockAt(chunkX+x, y, chunkZ+z).setType(Material.LAVA);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(event.getEntity().getGameMode().equals(GameMode.SURVIVAL)) {
            event.setCancelled(true);
            lavaUpdater.cancel();
            event.getEntity().sendMessage(ChallengePlugin.getPrefix() + ChatColor.RED + "No Jumps allowed. You failed!");
            event.getEntity().setGameMode(GameMode.SPECTATOR);
        }
    }
}
