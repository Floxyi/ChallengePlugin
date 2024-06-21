package de.floxyii.challengeplugin.challenge;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.challenges.*;
import de.floxyii.challengeplugin.challenge.modules.*;
import de.floxyii.challengeplugin.challenge.utils.Challenge;
import de.floxyii.challengeplugin.challenge.utils.Module;
import de.floxyii.challengeplugin.challenge.utils.Waypoint;
import de.floxyii.challengeplugin.utils.ChallengeConfig;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChallengeManager {

    private Challenge runningChallenge = null;
    private final List<Challenge> challengesList = new ArrayList<>();
    private final List<Module> modulesList = new ArrayList<>();
    private final List<Module> runningModules = new ArrayList<>();
    public final List<Waypoint> waypoints = new ArrayList<>();

    public ChallengeManager() {
        challengesList.add(new TheFloorIsLavaChallenge());
        challengesList.add(new NoDoubleItemChallenge());
        challengesList.add(new HalfHeartChallenge());
        challengesList.add(new NoJumpChallenge());

        modulesList.add(new SharedHeartsModule());
        modulesList.add(new HalfHeartModule());
        modulesList.add(new HardcoreModule());
        modulesList.add(new NoRegenModule());
    }

    public List<Challenge> getChallenges() {
        return challengesList;
    }

    public List<String> getChallengeNames() {
        List<String> names = new ArrayList<>();
        for (Challenge challenge : getChallenges()) {
            names.add(challenge.getName());
        }
        return names;
    }

    public Challenge getChallenge(String challengeName) {
        for (Challenge challenge : getChallenges()) {
            if (challenge.getName().equals(challengeName)) {
                return challenge;
            }
        }
        return null;
    }

    public Challenge getRunningChallenge() {
        return runningChallenge;
    }

    public List<Module> getModulesList() {
        return modulesList;
    }

    public List<String> getModuleNames() {
        List<String> names = new ArrayList<>();
        for (Module module : getModulesList()) {
            names.add(module.getName());
        }
        return names;
    }

    public Module getModule(String moduleName) {
        for (Module module : getModulesList()) {
            if (module.getName().equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public void setModule(Module module, boolean state) {
        if (runningChallenge != null) {
            boolean bool = module.setActive(state);
            if (!bool) {
                return;
            }
            if (state) {
                runningModules.add(module);
            } else {
                runningModules.remove(module);
            }
        }
    }

    public List<Module> getActiveModules() {
        return runningModules;
    }

    public void startChallenge(Challenge challenge) {
        if (runningChallenge == null) {
            runningChallenge = challenge;

            challenge.activateChallenge();
            ChallengePlugin.getChallengeTimer().resetTimer();
            ChallengePlugin.getChallengeTimer().startTimer();
            updateChallengeListHeader();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.SURVIVAL);
                resetPlayer(player);
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GOLD + runningChallenge.getName() + "-Challenge" + ChatColor.GREEN + " got started!");
            }

            World world = ChallengePlugin.getChallengeWorld();
            if (world != null) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            }
        }
    }

    private void resetPlayer(Player player) {
        player.setHealth(20);
        player.setSaturation(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setHeldItemSlot(0);
    }

    public void stopChallenge() {
        if (runningChallenge != null) {
            runningChallenge.stopChallenge();

            for (Module module : runningModules) {
                module.setActive(false);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.ADVENTURE);
                ChallengePlugin.getChallengeTimer().stopTimer();
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GOLD + runningChallenge.getName() + "-Challenge" + ChatColor.GREEN + " got stopped!");
            }

            World world = ChallengePlugin.getChallengeWorld();
            if (world != null) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }
        }
    }

    public boolean resumeChallenge() {
        if (runningChallenge != null) {

            if (runningChallenge.isActive()) {
                return false;
            }

            runningChallenge.resumeChallenge();
            ChallengePlugin.getChallengeTimer().startTimer();

            for (Module module : runningModules) {
                module.setActive(true);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GOLD + runningChallenge.getName() + "-Challenge" + ChatColor.GREEN + " got resumed!");
            }

            World world = ChallengePlugin.getChallengeWorld();
            if (world != null) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            }

            updateChallengeListHeader();
            return true;
        }

        return false;
    }

    public void removeChallenge() {
        if (runningChallenge != null) {
            runningChallenge.stopChallenge();

            for (Module module : runningModules) {
                module.setActive(false);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                ChallengePlugin.getChallengeTimer().removePlayer(player);
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(ChallengePlugin.getPrefix() + ChatColor.GOLD + runningChallenge.getName() + "-Challenge" + ChatColor.GREEN + " got removed!");
            }

            World world = ChallengePlugin.getChallengeWorld();
            if (world != null) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }

            runningChallenge = null;
            updateChallengeListHeader();
        }
    }

    public void endChallenge() {
        ChallengePlugin.getChallengeTimer().stopTimer();
        runningChallenge.stopChallenge();

        for (Module module : getModulesList()) {
            module.setActive(false);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(runningChallenge.getPrefix() + runningChallenge.getDeathMessage());
            player.sendMessage(runningChallenge.getPrefix() + "Time wasted: " + ChatColor.GOLD + ChallengePlugin.getChallengeTimer().getFormattedTime());
            player.setGameMode(GameMode.SPECTATOR);
            ChallengePlugin.getChallengeTimer().removePlayer(player);
        }

        runningChallenge = null;
        ChallengePlugin.getChallengeTimer().resetTimer();
        updateChallengeListHeader();
    }

    private void updateChallengeListHeader() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListHeader(getHeaderText());
        }
    }

    public String getHeaderText() {
        String text = ChatColor.GOLD + " Challenge Server (No Challenge) ";
        if (ChallengePlugin.getChallengeManager().getRunningChallenge() != null) {
            String challenge = ChallengePlugin.getChallengeManager().getRunningChallenge().getName();
            text = ChatColor.GOLD + " Challenge Server (" + challenge + ") ";
        }
        return text;
    }

    public Inventory getChallengeInventory() {
        int inventorySize = challengesList.size() % 9 > 0 ? (challengesList.size() / 9) + 9 : challengesList.size();
        Inventory inventory = Bukkit.createInventory(null, inventorySize, "Challenges");

        for (Challenge challenge : getChallenges()) {
            ItemStack item = new ItemStack(challenge.getDisplayItem());
            setName(item, ChatColor.GOLD + challenge.getName());
            setLore(item, challenge.getDescription());
            if (challenge.isActive()) {
                addGlow(item);
            }
            inventory.addItem(item);
        }

        return inventory;
    }

    private void setLore(ItemStack item, String loreText) {
    ItemMeta meta = item.getItemMeta();
    if (meta != null) {
        List<String> lore = new ArrayList<>();
        String[] lines = loreText.split("\n");
        Collections.addAll(lore, lines);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}

    private void addGlow(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
    }

    private void setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
    }

    public void saveChallengeState() {
        ChallengeConfig config = ChallengePlugin.getChallengeConfig();

        if (getRunningChallenge() != null) {
            runningChallenge.stopChallenge();
            config.set("challenge.type", runningChallenge.getName());
            runningChallenge.saveContents("challenge.contents");
        } else {
            config.set("challenge", "");
        }

        if (ChallengePlugin.getChallengeTimer().getTime() != 0) {
            ChallengePlugin.getChallengeTimer().stopTimer();
            config.set("timer.time", ChallengePlugin.getChallengeTimer().getTime());
            config.set("timer.active", true);
        } else {
            config.set("timer.active", false);
        }

        List<String> moduleNames = new ArrayList<>();
        for (Module module : getActiveModules()) {
            moduleNames.add(module.getName());
            module.setActive(false);
        }
        config.set("modules.active", moduleNames);

        if (!waypoints.isEmpty()) {
            config.set("waypoints.amount", waypoints.size());
            for (int i = 0; i < waypoints.size(); i++) {
                config.set("waypoints." + i + ".Name", waypoints.get(i).name);
                config.set("waypoints." + i + ".X", waypoints.get(i).x);
                config.set("waypoints." + i + ".Y", waypoints.get(i).y);
                config.set("waypoints." + i + ".Z", waypoints.get(i).z);
            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    public void loadChallengeState() {
        ChallengeConfig config = ChallengePlugin.getChallengeConfig();

        if (config.get("challenge") != null && config.get("challenge.type") != null) {
            Challenge challenge = getChallenge((String) config.get("challenge.type"));
            startChallenge(challenge);
            challenge.loadContents("challenge.contents");
            stopChallenge();
        }

        if (config.get("timer.active") != null) {
            boolean active = (Boolean) config.get("timer.active");
            if (active) {
                int time = (int) config.get("timer.time");
                ChallengePlugin.getChallengeTimer().setTimer(time);
                ChallengePlugin.getChallengeTimer().startTimer();
                ChallengePlugin.getChallengeTimer().stopTimer();
            }
        }

        if (config.get("modules.active") != null) {
            List<String> moduleNames = (List<String>) config.get("modules.active");
            for (String moduleName : moduleNames) {
                setModule(getModule(moduleName), true);
            }
        }

        if (config.get("waypoints.amount") != null) {
            for (int i = 0; i < (int) config.get("waypoints.amount"); i++) {
                String name = (String) config.get("waypoints." + i + ".Name");
                double x = (double) config.get("waypoints." + i + ".X");
                double y = (double) config.get("waypoints." + i + ".Y");
                double z = (double) config.get("waypoints." + i + ".Z");

                waypoints.add(new Waypoint(x, y, z, name));
            }
        }
    }
}
