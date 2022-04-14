package de.floxyii.challengeplugin;

import de.floxyii.challengeplugin.challenge.ChallengeManager;
import de.floxyii.challengeplugin.commands.ChallengeCommand;
import de.floxyii.challengeplugin.commands.ModuleCommand;
import de.floxyii.challengeplugin.commands.TimerCommand;
import de.floxyii.challengeplugin.challenge.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ChallengePlugin extends JavaPlugin {

    private static ChallengePlugin plugin;
    private static ChallengeManager challengeManager;
    private static Timer timer;

    @Override
    public void onEnable() {
        plugin = this;
        challengeManager = new ChallengeManager();

        Bukkit.getLogger().info(getPrefix() + "Plugin got activated!");

        timer = new Timer();
        commandRegistration();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(getPrefix() + "Plugin got disabled!");
    }

    public void commandRegistration() {
        Objects.requireNonNull(getCommand("timer")).setExecutor(new TimerCommand());
        Objects.requireNonNull(getCommand("challenge")).setExecutor(new ChallengeCommand());
        Objects.requireNonNull(getCommand("module")).setExecutor(new ModuleCommand());
    }

    public static String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + "Challenge Plugin" + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    public static ChallengePlugin getPlugin() {
        return plugin;
    }

    public static World getChallengeWorld() {
        return Bukkit.getWorld("world");
    }

    public static ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    public static Timer getChallengeTimer() {
        return timer;
    }
}
