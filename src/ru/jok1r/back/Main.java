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
import java.util.Map;

public class Main extends JavaPlugin {

    public static Main instance;
    public static Map<String, Location> backMap = new HashMap();
    public static List<String> usesTp = new ArrayList();
    public static Map<String, Long> cooldowns = new HashMap();

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
        if (cmd.getLabel().equalsIgnoreCase("dback")) {
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

                    if(!backMap.containsKey(player.getDisplayName())) {
                        sendMessageToPlayer(player, getConfig().getString("messages.noDeath"));
                        return false;
                    }

                    if(cooldowns.containsKey(player.getDisplayName()) && System.currentTimeMillis() < cooldowns.get(player.getDisplayName())) {
                        String cdMsg = getConfig().getString("messages.cooldown");
                        long cd = (cooldowns.get(player.getDisplayName()) - System.currentTimeMillis()) / 1000;
                        cdMsg = cdMsg.replaceAll("\\%time\\%", cd + "");
                        sendMessageToPlayer(player, cdMsg);
                        return false;
                    }

                    Location loc = backMap.get(player.getDisplayName());
                    player.teleport(loc);
                    usesTp.add(player.getDisplayName());
                    sendMessageToPlayer(player, getConfig().getString("messages.teleport"));
                    cooldowns.put(player.getDisplayName(), System.currentTimeMillis() + (getConfig().getInt("cooldown") * 1000));
                }
            }
        }
        if (cmd.getLabel().equalsIgnoreCase("dbackreload")) {
            if(sender instanceof Player && !sender.isOp()) {
                Player player = (Player)sender;
                sendMessageToPlayer(player, getConfig().getString("messages.noPerm"));
                return false;
            }

            if(!file.exists()) {
                saveDefaultConfig();
                getConfig().options().copyDefaults(true);
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
