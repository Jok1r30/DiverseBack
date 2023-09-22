package ru.jok1r.back;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final Main plugin;

    public PlayerDeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player != null && player.hasPermission("diverseBack.use")) {
            this.plugin.backMap.put(player.getName(), player.getLocation());
            this.plugin.usesTp.remove(player.getName());
        }

    }
}