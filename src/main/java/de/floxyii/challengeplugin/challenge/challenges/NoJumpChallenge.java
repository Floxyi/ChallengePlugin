package de.floxyii.challengeplugin.challenge.challenges;

import com.google.common.collect.Sets;
import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.utils.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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
        return "In this challenge you are not allowed to jump!\nSo be carefully going down a deep cave without the\n" +
                "needed equipment to get out again!\nHave a great time and please do not cheat\nby unbinding your jump key!";
    }

    @Override
    public Material getDisplayItem() {
        return Material.DIAMOND_BOOTS;
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

    private final Set<UUID> prevPlayersOnGround = Sets.newHashSet();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (player.getVelocity().getY() > 0) {
            double jumpVelocity = 0.42F;
            if (player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
                jumpVelocity += (float) (Objects.requireNonNull(player.getPotionEffect(PotionEffectType.JUMP_BOOST)).getAmplifier() + 1) * 0.1F;
            }
            if (e.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!((LivingEntity) player).isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
                    failedPlayer = player;
                    ChallengePlugin.getChallengeManager().endChallenge();
                }
            }
        }

        if (((LivingEntity) player).isOnGround()) {
            prevPlayersOnGround.add(player.getUniqueId());
        } else {
            prevPlayersOnGround.remove(player.getUniqueId());
        }
    }
}
