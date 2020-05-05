package ru.jok1r.restart;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRestart implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player && !sender.isOp()) {
            Main.sendMessage((Player)sender, Main.noPerm);
            return false;
        }

        if(args.length == 0) {
            Main.sendMessage((Player)sender, Main.help);
            return false;
        }

        if(args[0].equals("start")) {
            if(Main.restart) {
                Main.sendMessage((Player)sender, Main.restartFail);
                return false;
            }
            if(args.length == 2 && Integer.valueOf(args[1]) != 0){
                Main.startRestart(Integer.valueOf(args[1]));
                return false;
            }

            Main.sendMessage((Player)sender, Main.restartNoTime);
            return false;
        }

        if(args[0].equals("stop")) {
            if(Main.restart == false) {
                Main.sendMessage((Player) sender, Main.restartFail2);
                return false;
            }
            Main.stopRestart();
            return false;
        }

        Main.sendMessage((Player)sender, Main.help);
        return false;
    }
}
