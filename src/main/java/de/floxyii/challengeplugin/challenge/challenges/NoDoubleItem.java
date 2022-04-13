package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class NoDoubleItem implements Listener, Challenge {
    @Override
    public String getName() {
        return "NoDoubleInvItem";
    }

    @Override
    public void onActivate() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(getPrefix() + ChatColor.GREEN + getName() + "-Challenge got activated!");
        }
        addListeners();
    }

    @Override
    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(this, ChallengePlugin.getPlugin());
    }

    Item wrongItem;

    @EventHandler
    private void ItemCollectEvent(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.equals(event.getEntity())) {
                return;
            }

            if(player.getInventory().contains(event.getItem().getItemStack())) {
                wrongItem = event.getItem();
                event.setCancelled(true);
                ChallengePlugin.getChallengeManager().endChallenge();
            }
        }
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
    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + getName() + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    @Override
    public String getDeathMessage() {
        return ChatColor.RED + "No doubled items in your inventory allowed (" + wrongItem.getItemStack().displayName() + "). You failed!";
    }
}
