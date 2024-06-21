package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.utils.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.Objects;

public class NoDoubleItemChallenge implements Listener, Challenge {

    boolean isActive = false;
    Item wrongItem = null;
    @Override
    public String getName() {
        return "NoDoubleInvItem";
    }

    @Override
    public void activateChallenge() {
        wrongItem = null;

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

    @Override
    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    @EventHandler
    private void ItemCollectEvent(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.equals(event.getEntity())) {
                return;
            }

            if(player.getInventory().contains(event.getItem().getItemStack().getType())) {
                wrongItem = event.getItem();
                event.setCancelled(true);
                ChallengePlugin.getChallengeManager().endChallenge();
            }
        }
    }

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
    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    @Override
    public String getDescription() {
        return "As the name suggest you and your friends arent allowed\nto have the same item in your inventories!\nSo" +
                "make sure that you asked your friend before you pick up an item!\nMaybe you specialize on different " +
                "item categories to make it easier.";
    }

    @Override
    public Material getDisplayItem() {
        return Material.CHEST;
    }

    @Override
    public String getDeathMessage() {
        return ChatColor.RED + "No doubled items in your inventory allowed (" +
                Objects.requireNonNull(wrongItem.getItemStack().getItemMeta()).getDisplayName() +
                "). You failed!";
    }

    @Override
    public void saveContents(String path) {}

    @Override
    public void loadContents(String path) {}
}
