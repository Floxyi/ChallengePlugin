package de.floxyii.challengeplugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd("\u00A76\u00A7l|| Minecraft Challenge Server ||                    " +
                "\u00A7c\u00a7a Made by Floxyii with\u00a7c ❤");
    }

}
