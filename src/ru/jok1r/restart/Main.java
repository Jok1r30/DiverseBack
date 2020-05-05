package ru.jok1r.restart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public class Main extends JavaPlugin {

    File file = new File(getDataFolder(), "config.yml");
    public static String restartStop, restartIsComing, restartIsComing2, noPerm, restartFail, help, prefix, restartFail2, restartNoTime;
    public static boolean restart = false;
    static BukkitTask task;
    static Main instance;

    static int time, timeEx = 0;

    public void onEnable() {
        instance = this;
        if(!file.exists()) {
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);
            registerMessages();

            getCommand("drestart").setExecutor(new CommandRestart());
        }
    }

    public static void startRestart(int time2) {
        restart = true;
        time = time2 * 60;
        Main.instance.runTimer();
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageCompile(restartIsComing)));
    }

    public static void stopRestart() {
        restart = false;
        task.cancel();
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageCompile(restartStop)));
    }

    private void registerMessages() {
        prefix = getConfig().getString("messages.prefix");
        restartIsComing = getConfig().getString("messages.restartIsComing");
        restartIsComing2 = getConfig().getString("messages.restartIsComing2");
        restartStop = getConfig().getString("messages.restartStop");
        noPerm = getConfig().getString("messages.noPerm");
        restartFail = getConfig().getString("messages.restartFail");
        restartFail2 = getConfig().getString("messages.restartFail2");
        help = getConfig().getString("messages.help");
        restartNoTime = getConfig().getString("messages.restartNoTime");
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageCompile(message)));
    }

    private static String messageCompile(String message) {
        message = message.replaceAll("\\%prefix\\%", prefix);
        message = message.replaceAll("\\%time\\%", (time - timeEx) / 60 + "");
        message = message.replaceAll("\\%time2\\%", time - timeEx + "");

        return message;
    }

    public void runTimer() {
        task = Bukkit.getScheduler().runTaskTimer(Main.instance, new BukkitRunnable() {
            public void run () {
                timeEx++;
                if(timeEx >= time) {
                    restart = false;
                    time = 0;
                    task.cancel();

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
                    return;
                }

                if((time - timeEx) == 30) {
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageCompile(restartIsComing2)));
                    return;
                }

                if((time - timeEx) <= 15) {
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageCompile(restartIsComing2)));
                    return;
                }

                if(timeEx % 60 == 0) {
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageCompile(restartIsComing)));
                    return;
                }
            }
        }, 0L, 20L);
    }
}
