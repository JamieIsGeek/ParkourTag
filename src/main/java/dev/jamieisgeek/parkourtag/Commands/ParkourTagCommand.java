package dev.jamieisgeek.parkourtag.Commands;

import dev.jamieisgeek.parkourtag.Utils.PTUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParkourTagCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;
            String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ParkourTag" + ChatColor.stripColor("") + ChatColor.WHITE + "] ";

            if(args.length != 1) {
                p.sendMessage(prefix + ChatColor.WHITE + "Invalid Arguments. /pt [join | start | end]");
            } else if(args[0] == "join") {
                try {
                    PTUtils.joinGame(p, prefix);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
