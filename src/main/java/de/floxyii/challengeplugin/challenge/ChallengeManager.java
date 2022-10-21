package de.floxyii.challengeplugin.challenge;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.challenges.*;
import de.floxyii.challengeplugin.challenge.modules.*;
import de.floxyii.challengeplugin.challenge.modules.Module;
import de.floxyii.challengeplugin.utils.ChallengeConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChallengeManager {

    private final List<Challenge> challengesList = new ArrayList<>();
    private Challenge runningChallenge = null;
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
        for(Challenge challenge : getChallenges()) {
            names.add(challenge.getName());
        }
        return names;
    }

    public Challenge getChallenge(String challengeName) {
        for(Challenge challenge : getChallenges()) {
            if(challenge.getName().equals(challengeName)) {
                return challenge;
            }
        }
        return null;
    }

    public Challenge getRunningChallenge() {
        return runningChallenge;
    }

    public void clearRunningChallenge() {
        stopChallenge();
        for(Player player : Bukkit.getOnlinePlayers()) {
            ChallengePlugin.getChallengeTimer().removePlayer(player);
        }
        runningChallenge = null;
    }

    public List<Module> getModulesList() {
        return modulesList;
    }

    public List<String> getModuleNames() {
        List<String> names = new ArrayList<>();
        for(Module module : getModulesList()) {
            names.add(module.getName());
        }
        return names;
    }

    public Module getModule(String moduleName) {
        for(Module module : getModulesList()) {
            if(module.getName().equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public void setModule(Module module, boolean state) {
        if(runningChallenge != null) {
            boolean bool = module.setActive(state);
            if(!bool) {
                return;
            }
            if(state) {
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
        if(runningChallenge == null) {
            runningChallenge = challenge;

            challenge.activateChallenge();
            ChallengePlugin.getChallengeTimer().resetTimer();
            ChallengePlugin.getChallengeTimer().startTimer();
        }
    }

    public void stopChallenge() {
        if(runningChallenge != null) {
            runningChallenge.stopChallenge();
            ChallengePlugin.getChallengeTimer().stopTimer();

            for(Module module : runningModules) {
                module.setActive(false);
            }
        }
    }

    public boolean resumeChallenge() {
        if(runningChallenge != null) {

            if(runningChallenge.isActive()) {
                return false;
            }

            runningChallenge.resumeChallenge();
            ChallengePlugin.getChallengeTimer().startTimer();

            for(Module module : runningModules) {
                module.setActive(true);
            }

            return true;
        }
        return false;
    }

    public void endChallenge() {
        ChallengePlugin.getChallengeTimer().stopTimer();
        runningChallenge.stopChallenge();

        for(Module module : getModulesList()) {
            module.setActive(false);
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(runningChallenge.getPrefix() + runningChallenge.getDeathMessage());
            player.sendMessage(runningChallenge.getPrefix() + "Time wasted: " + ChatColor.GOLD + ChallengePlugin.getChallengeTimer().getFormattedTime() + ChatColor.RESET + " [ID: " + ChallengePlugin.getChallengeTimer().getTime() + "]");
            player.setGameMode(GameMode.SPECTATOR);
            ChallengePlugin.getChallengeTimer().removePlayer(player);
        }

        ChallengePlugin.getChallengeTimer().resetTimer();
        runningChallenge = null;
    }

    public void saveChallengeState() {
        ChallengeConfig config = ChallengePlugin.getChallengeConfig();

        if(getRunningChallenge() != null) {
            runningChallenge.stopChallenge();
            config.set("challenge.type", runningChallenge.getName());
            runningChallenge.saveContents("challenge.contents");
            config.set("challenge.active", true);
        } else {
            config.set("challenge.active", false);
        }

        if(ChallengePlugin.getChallengeTimer().getTime() != 0) {
            ChallengePlugin.getChallengeTimer().stopTimer();
            config.set("timer.time", ChallengePlugin.getChallengeTimer().getTime());
            config.set("timer.active", true);
        } else {
            config.set("timer.active", false);
        }

        List<String> moduleNames = new ArrayList<>();
        for(Module module : getActiveModules()) {
            moduleNames.add(module.getName());
            module.setActive(false);
        }
        config.set("modules.active", moduleNames);

        if(!waypoints.isEmpty()) {
            config.set("waypoints.amount", waypoints.size());
            for(int i = 0; i < waypoints.size(); i++) {
                config.set("waypoints." + i + ".Name", waypoints.get(i).name);
                config.set("waypoints." + i + ".X", waypoints.get(i).x);
                config.set("waypoints." + i + ".Y", waypoints.get(i).y);
                config.set("waypoints." + i + ".Z", waypoints.get(i).z);
            }
        }
    }

    @SuppressWarnings (value="unchecked")
    public void loadChallengeState() {
        ChallengeConfig config = ChallengePlugin.getChallengeConfig();

        if(config.get("challenge.active") != null) {
            boolean active = (Boolean) config.get("challenge.active");
            if(active) {
                String challengeName = (String) config.get("challenge.type");
                Challenge challenge = getChallenge(challengeName);
                startChallenge(challenge);
                challenge.loadContents("challenge.contents");
            }
        }

        if(config.get("timer.active") != null) {
            boolean active = (Boolean) config.get("timer.active");
            if (active) {
                int time = (int) config.get("timer.time");
                ChallengePlugin.getChallengeTimer().setTimer(time);
                ChallengePlugin.getChallengeTimer().startTimer();
                ChallengePlugin.getChallengeTimer().stopTimer();
            }
        }

        if(config.get("modules.active") != null) {
            List<String> moduleNames = (List<String>) config.get("modules.active");
            for(String moduleName : moduleNames) {
                setModule(getModule(moduleName), true);
            }
        }

        if(config.get("waypoints.amount") != null) {
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
