package ru.jok1r.back;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    public Main instance;
    public static HashMap backMap = new HashMap<String, Location>();
    public static List usesTp = new ArrayList<String>();

    File file = new File(getDataFolder(), "config.yml");

    public void onEnable() {
        instance = this;
        if(!file.exists()) {
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);
        }

        getLogger().severe(">> Плагин на телепортацию до последней точки смерти игрока, автор Jok1r, vk.com/jok1rstyle, vk.com/diverselab");
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new Events(), (Plugin)this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getLabel().equalsIgnoreCase("back")) {
            if (!(sender instanceof Player)) {
                return false;
            } else {
                Player player = (Player)sender;
                if(!player.isOp() && !player.hasPermission("diverseBack.use")) {
                    sendMessageToPlayer(player, getConfig().getString("messages.noPerm"));
                } else {
                    if(usesTp.contains(player.getDisplayName())) {
                        sendMessageToPlayer(player, getConfig().getString("messages.cantTp"));
                        return false;
                    }

                    if (backMap.containsKey(player.getDisplayName())) {
                        Location loc = (Location) backMap.get(player.getDisplayName());
                        player.teleport(loc);
                        usesTp.add(player.getDisplayName());
                        sendMessageToPlayer(player, getConfig().getString("messages.teleport"));
                    } else {
                        sendMessageToPlayer(player, getConfig().getString("messages.noDeath"));
                    }
                }
            }
        }
        if (cmd.getLabel().equalsIgnoreCase("dbackreload")) {
            if(sender instanceof Player && !sender.isOp()) {
                Player player = (Player)sender;
                sendMessageToPlayer(player, getConfig().getString("messages.noPerm"));
                return false;
            }

            reloadConfig();
            getConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.reload")));
        }

        return false;
    }

    public void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
