package de.floxyii.challengeplugin.challenge;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.challenges.Challenge;
import de.floxyii.challengeplugin.challenge.challenges.NoDoubleItemChallenge;
import de.floxyii.challengeplugin.challenge.challenges.NoJumpChallenge;
import de.floxyii.challengeplugin.challenge.challenges.TheFloorIsLavaChallenge;
import de.floxyii.challengeplugin.challenge.modules.HardcoreModule;
import de.floxyii.challengeplugin.challenge.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChallengeManager {

    Challenge runningChallenge = null;
    List<Challenge> challengesList = new ArrayList<>();
    List<Module> modules = new ArrayList<>();
    List<Module> runningModules = new ArrayList<>();

    public ChallengeManager() {
        challengesList.add(new TheFloorIsLavaChallenge());
        challengesList.add(new NoJumpChallenge());
        challengesList.add(new NoDoubleItemChallenge());

        modules.add(new HardcoreModule());
        // modules.add("NoHeartRegen");
        // modules.add("Shared Hearts");
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

    public List<Module> getModules() {
        return modules;
    }

    public List<String> getModuleNames() {
        List<String> names = new ArrayList<>();
        for(Module module : getModules()) {
            names.add(module.getName());
        }
        return names;
    }

    public Module getModule(String moduleName) {
        for(Module module : getModules()) {
            if(module.getName().equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public boolean setModule(Module module, boolean state) {
        if(runningChallenge != null) {
            return module.setActive(state);
        }

        if(state) {
            runningModules.add(module);
        } else {
            runningModules.remove(module);
        }
        return false;
    }

    public boolean startChallenge(Challenge challenge) {
        if(runningChallenge == null) {
            runningChallenge = challenge;

            challenge.activateChallenge();
            ChallengePlugin.getChallengeTimer().resetTimer();
            ChallengePlugin.getChallengeTimer().startTimer();

            return true;
        }
        return false;
    }

    public boolean stopChallenge() {
        if(runningChallenge != null) {
            runningChallenge.stopChallenge();
            ChallengePlugin.getChallengeTimer().stopTimer();

            for(Module module : getModules()) {
                module.setActive(false);
            }

            return true;
        }
        return false;
    }

    public boolean resumeChallenge() {
        if(runningChallenge != null) {
            runningChallenge.resumeChallenge();
            ChallengePlugin.getChallengeTimer().startTimer();

            for(Module module : runningModules) {
                module.setActive(false);
            }

            return true;
        }
        return false;
    }

    public void endChallenge() {
        ChallengePlugin.getChallengeTimer().stopTimer();
        runningChallenge.stopChallenge();

        for(Module module : getModules()) {
            module.setActive(false);
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(runningChallenge.getPrefix() + runningChallenge.getDeathMessage());
            player.sendMessage(runningChallenge.getPrefix() + "Time wasted: " + ChatColor.GOLD + ChallengePlugin.getChallengeTimer().getFormattedTime());
            player.setGameMode(GameMode.SPECTATOR);
        }

        runningChallenge = null;
    }

}
