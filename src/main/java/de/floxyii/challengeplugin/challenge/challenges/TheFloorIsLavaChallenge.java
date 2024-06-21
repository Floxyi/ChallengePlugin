package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.utils.Challenge;
import de.floxyii.challengeplugin.utils.ChallengeConfig;
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

public class TheFloorIsLavaChallenge implements Listener, Challenge {

    boolean isActive = false;
    int height;
    BukkitTask lavaUpdater;

    @Override
    public String getName() {
        return "TheFloorIsLava";
    }

    @Override
    public void activateChallenge() {
        height = -63;
        updateLoadedChunks();
        updateLavaHeight();
        registerListener();
        isActive = true;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    private void updateLoadedChunks() {
        for(Chunk chunk : ChallengePlugin.getChallengeWorld().getLoadedChunks()) {
            int chunkX = chunk.getX() * 16;
            int chunkZ = chunk.getZ() * 16;

            for (int x = 0; x <= 16; x++) {
                for (int z = 0; z <= 16; z++) {
                    if (chunk.getWorld().getBlockAt(chunkX + x, height, chunkZ + z).getType() == Material.AIR || chunk.getWorld().getBlockAt(chunkX + x, height, chunkZ + z).getType() == Material.CAVE_AIR) {
                        chunk.getWorld().getBlockAt(chunkX + x, height, chunkZ + z).setType(Material.LAVA);
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
        }.runTaskTimer(ChallengePlugin.getPlugin(), 0, 150);
    }

    @Override
    public void stopChallenge() {
        HandlerList.unregisterAll(this);
        lavaUpdater.cancel();
        isActive = false;
    }

    @Override
    public void resumeChallenge() {
        updateLavaHeight();
        registerListener();
        isActive = true;
    }

    @Override
    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "In this challenge lava will raise from the very bottom of your world!\nYou need to survive as long as " +
                "you can while getting the most important\nmaterials first before they are under lava!\nChallenge your " +
                "friends or take the challenge for\nyourself and try to achieve the best time!";
    }

    @Override
    public Material getDisplayItem() {
        return Material.LAVA_BUCKET;
    }

    @Override
    public String getDeathMessage() {
        return ChatColor.RED + "The floor is lava!. You failed!";
    }

    @Override
    public void saveContents(String path) {
        ChallengeConfig config = ChallengePlugin.getChallengeConfig();
        config.set(path + ".height", height);
    }

    @Override
    public void loadContents(String path) {
        ChallengeConfig config = ChallengePlugin.getChallengeConfig();
        if(config.get(path + ".height") != null) {
            height = (int) config.get(path + ".height");
        }
    }

    @Override
    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(!(event.getEntity().getGameMode().equals(GameMode.SURVIVAL) && Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause().equals(EntityDamageEvent.DamageCause.LAVA))) {
            return;
        }

        ChallengePlugin.getChallengeManager().endChallenge();
    }
}
