package de.floxyii.challengeplugin;

import de.floxyii.challengeplugin.challenge.ChallengeManager;
import de.floxyii.challengeplugin.commands.ChallengeCommand;
import de.floxyii.challengeplugin.commands.ModuleCommand;
import de.floxyii.challengeplugin.commands.TimerCommand;
import de.floxyii.challengeplugin.challenge.Timer;
import de.floxyii.challengeplugin.commands.WaypointCommand;
import de.floxyii.challengeplugin.listener.JoinListener;
import de.floxyii.challengeplugin.listener.PingListener;
import de.floxyii.challengeplugin.utils.ChallengeConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ChallengePlugin extends JavaPlugin {

    private static ChallengePlugin plugin;
    private static ChallengeManager challengeManager;
    private static Timer timer;
    private static ChallengeConfig challengeConfig;

    @Override
    public void onEnable() {
        plugin = this;
        challengeConfig = new ChallengeConfig();
        challengeManager = new ChallengeManager();

        Bukkit.getLogger().info(getPrefix() + "Challenge-Plugin got activated!");

        timer = new Timer();

        commandRegistration();
        listenerRegistration();

        getChallengeManager().loadChallengeState();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(getPrefix() + "Challenge-Plugin got disabled!");
        getChallengeManager().saveChallengeState();
    }

    public void commandRegistration() {
        Objects.requireNonNull(getCommand("timer")).setExecutor(new TimerCommand());
        Objects.requireNonNull(getCommand("challenge")).setExecutor(new ChallengeCommand());
        Objects.requireNonNull(getCommand("module")).setExecutor(new ModuleCommand());
        Objects.requireNonNull(getCommand("waypoint")).setExecutor(new WaypointCommand());
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PingListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
    }

    public static String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + "Challenge-Plugin" + ChatColor.GRAY + "] " + ChatColor.RESET;
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

    public static ChallengeConfig getChallengeConfig() {
        return challengeConfig;
    }
}
