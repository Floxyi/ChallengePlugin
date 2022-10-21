package de.floxyii.challengeplugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd("§6§l|| Minecraft Challenge Server ||                §c§a Made by Floxyii with§c ❤");
    }

}
