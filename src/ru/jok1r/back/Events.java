package ru.jok1r.back;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.List;

public class Events implements Listener {

    HashMap backMap = Main.backMap;
    List usesTp = Main.usesTp;

    @EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(player != null && player.hasPermission("diverseBack.use")) {
            if(!backMap.containsKey(player.getDisplayName())) {
                backMap.put(player.getDisplayName(), player.getLocation());
            } else {
                if(usesTp.contains(player.getDisplayName())) {
                    usesTp.remove(player.getDisplayName());
                }
                backMap.put(player.getDisplayName(), player.getLocation());
            }
        }
    }
}
