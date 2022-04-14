package de.floxyii.challengeplugin.challenge;

import de.floxyii.challengeplugin.ChallengePlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Timer {

    boolean isRunning;
    int time = 0;
    BukkitTask updater;
    ArrayList<Player> players = new ArrayList<>();

    public Timer() {
        players.addAll(Bukkit.getOnlinePlayers());
    }

    public boolean startTimer() {
        if(isRunning) {
            return false;
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!players.contains(player)) {
                players.add(player);
            }
        }

        isRunning = true;
        if(updater == null) {
            updateTimer();
        }

        return true;
    }

    public boolean stopTimer() {
        if(isRunning) {
            isRunning = false;
            return true;
        }

        return false;
    }

    public boolean resetTimer() {
        if(updater == null) {
            return false;
        }

        stopTimer();
        time = 0;
        return true;
    }

    public boolean removePlayer(Player player) {
        if(players.contains(player)) {
            players.remove(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
            return true;
        }
        return false;
    }

    private void updateTimer() {
        updater = new BukkitRunnable() {

            @Override
            public void run() {
                if(isRunning) {
                    time++;
                    timerText(ChatColor.GOLD + getFormattedTime());
                } else {
                    timerText(ChatColor.GOLD + getFormattedTime() + ChatColor.RED + " (stopped)");
                }
            }

        }.runTaskTimer(ChallengePlugin.getPlugin(), 0, 20);
    }

    public String getFormattedTime() {
        String secondsSTR;
        String minutesSTR;
        String hoursSTR;

        int seconds = time % 60;
        int minutes = (time / 60) % 60;
        int hours = (time / 360) % 60;


        if(seconds < 10) {
            secondsSTR = "0" + seconds;
        } else {
            secondsSTR = "" + seconds;
        }
        if(minutes < 10) {
            minutesSTR = "0" + minutes;
        } else {
            minutesSTR = "" + minutes;
        }
        if(hours < 10) {
            hoursSTR = "0" + hours;
        } else {
            hoursSTR = "" + hours;
        }

        return hoursSTR + ":" + minutesSTR + ":" + secondsSTR;
    }

    private void timerText(String text) {
        for (Player player : players) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
        }
    }

}
