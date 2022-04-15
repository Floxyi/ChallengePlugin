package de.floxyii.challengeplugin.challenge;

import org.bukkit.ChatColor;

public class Waypoint {

    private final double x;
    private final double y;
    private final double z;
    public final String name;

    public Waypoint(double x, double y, double z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public String getCoordinates() {
        double roundX = Math.round(x * 10) / 10.0;
        double roundY = Math.round(y * 10) / 10.0;
        double roundZ = Math.round(z * 10) / 10.0;

        return ChatColor.GOLD + name + ": " + ChatColor.RESET + "x: " + roundX + " | y: " + roundY + " | z: " + roundZ + ChatColor.RESET;
    }
}
