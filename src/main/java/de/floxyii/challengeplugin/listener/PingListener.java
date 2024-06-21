package de.floxyii.challengeplugin.listener;

import de.floxyii.challengeplugin.ChallengePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String challenge = "No Challenge";

        if(ChallengePlugin.getChallengeManager().getRunningChallenge() != null) {
            challenge = ChallengePlugin.getChallengeManager().getRunningChallenge().getName();
        }

        event.setMotd("§6§lChallenge Server (" + challenge + ")\n§c§aMade by Floxyii with§c ❤");
    }

}
