package de.floxyii.challengeplugin.challenge;

import de.floxyii.challengeplugin.ChallengePlugin;
import de.floxyii.challengeplugin.challenge.challenges.NoDoubleItem;
import de.floxyii.challengeplugin.challenge.challenges.NoJump;
import de.floxyii.challengeplugin.challenge.challenges.TheFloorIsLava;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChallengeManager {

    Challenge runningChallenge = null;

    public List<Challenge> getChallenges() {
        List<Challenge> challengesList = new ArrayList<>();
        challengesList.add(new TheFloorIsLava());
        challengesList.add(new NoJump());
        challengesList.add(new NoDoubleItem());
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

    public boolean startChallenge(Challenge challenge) {
        challenge.onActivate();

        if(runningChallenge == null) {
            runningChallenge = challenge;
            return true;
        }
        return false;
    }

    public boolean stopChallenge() {
        if(runningChallenge != null) {
            runningChallenge.stopChallenge();
            return true;
        }
        return false;
    }

    public boolean resumeChallenge() {
        if(runningChallenge != null) {
            runningChallenge.resumeChallenge();
            return true;
        }
        return false;
    }

    public void endChallenge() {
        ChallengePlugin.getChallengeTimer().stopTimer();
        runningChallenge.stopChallenge();

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(runningChallenge.getPrefix() + runningChallenge.getDeathMessage());
            player.sendMessage(runningChallenge.getPrefix() + "Time wasted: " + ChatColor.GOLD + ChallengePlugin.getChallengeTimer().getFormattedTime());
            player.setGameMode(GameMode.SPECTATOR);
        }

        runningChallenge = null;
    }

}
