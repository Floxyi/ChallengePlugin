package de.floxyii.challengeplugin.challenge.challenges;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class NoDoubleItemChallenge implements Listener, Challenge {

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
            player.sendMessage(getPrefix() + ChatColor.GREEN + getName() + "-Challenge got activated!");
        }
        addListeners();
    }

    @Override
    public void addListeners() {
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
    public String getDescription() {
        return "As the name suggest you and your friends arent allowed to have the same item in your inventories! So" +
                "make sure that you asked your friend before you pick up an item! Maybe you specialize on different " +
                "item categories to make it easier.";
    }

    @Override
    public String getDeathMessage() {
        return ChatColor.RED + "No doubled items in your inventory allowed (" + wrongItem.getItemStack().displayName() + "). You failed!";
    }
}
