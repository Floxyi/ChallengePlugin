package de.floxyii.challengeplugin.listener;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getView().getTitle().equals("Challenges")) {
            event.setCancelled(true);
            ChallengePlugin.getChallengeManager().getChallenges().forEach(challenge -> {
                if (Objects.requireNonNull(event.getCurrentItem()).getType().equals(challenge.getDisplayItem())) {
                    ChallengePlugin.getChallengeManager().startChallenge(challenge);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(ChallengePlugin.getChallengeManager().getChallengeInventory());
                }
            });
        }
    }
}
