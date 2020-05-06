package ru.jok1r.back;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.List;
import java.util.Map;

public class Events implements Listener {

    Map backMap = Main.backMap;
    List usesTp = Main.usesTp;

    @EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(player != null && player.hasPermission("diverseBack.use")) {
            backMap.put(player.getDisplayName(), player.getLocation());
            if(usesTp.contains(player.getDisplayName())) {
                usesTp.remove(player.getDisplayName());
            }
        }
    }
}
