package ru.jok1r.back;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    public Map<String, Location> backMap = Maps.newHashMap();
    public List<String> usesTp = Lists.newArrayList();
    public Map<String, Long> cooldowns = Maps.newHashMap();
    public File file = new File(this.getDataFolder(), "config.yml");

    public void onEnable() {
        if (!this.file.exists()) {
            this.saveDefaultConfig();
            this.getConfig().options().copyDefaults(true);
        }

        this.getLogger().severe(">> Плагин на телепортацию до последней точки смерти игрока, автор Jok1r, vk.com/jok1rstyle, vk.com/diverselab");
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player;
        if (cmd.getLabel().equalsIgnoreCase("dback")) {
            if (!(sender instanceof Player)) {
                return false;
            }

            player = (Player)sender;
            if (!player.isOp() && !player.hasPermission("diverseBack.use")) {
                this.sendMessageToPlayer(player, this.getConfig().getString("messages.noPerm"));
            } else {
                if (usesTp.contains(player.getDisplayName())) {
                    this.sendMessageToPlayer(player, this.getConfig().getString("messages.cantTp"));
                    return false;
                }

                if (!backMap.containsKey(player.getDisplayName())) {
                    this.sendMessageToPlayer(player, this.getConfig().getString("messages.noDeath"));
                    return false;
                }

                if (cooldowns.containsKey(player.getDisplayName()) && System.currentTimeMillis() < cooldowns.get(player.getDisplayName())) {
                    String cdMsg = this.getConfig().getString("messages.cooldown");
                    long cd = (cooldowns.get(player.getDisplayName()) - System.currentTimeMillis()) / 1000L;
                    cdMsg = cdMsg.replaceAll("\\%time\\%", cd + "");
                    this.sendMessageToPlayer(player, cdMsg);
                    return false;
                }

                Location loc = backMap.get(player.getDisplayName());
                player.teleport(loc);
                usesTp.add(player.getDisplayName());
                this.sendMessageToPlayer(player, this.getConfig().getString("messages.teleport"));
                cooldowns.put(player.getDisplayName(), System.currentTimeMillis() + (this.getConfig().getInt("cooldown") * 1000L));
            }
        }

        if (cmd.getLabel().equalsIgnoreCase("dbackreload")) {
            if (sender instanceof Player && !sender.isOp()) {
                player = (Player)sender;
                this.sendMessageToPlayer(player, this.getConfig().getString("messages.noPerm"));
                return false;
            }

            if (!this.file.exists()) {
                this.saveDefaultConfig();
                this.getConfig().options().copyDefaults(true);
            }

            this.reloadConfig();
            this.getConfig();
            this.sendMessageToPlayer(sender, this.getConfig().getString("messages.reload"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.reload")));
        }

        return false;
    }

    public void sendMessageToPlayer(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}