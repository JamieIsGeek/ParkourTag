package dev.jamieisgeek.parkourtag.Commands;

import dev.jamieisgeek.parkourtag.Utils.PTUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ParkourTagCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;
            String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ParkourTag" + ChatColor.stripColor("") + ChatColor.WHITE + "] ";

            if(args.length != 1) {
                p.sendMessage(prefix + ChatColor.WHITE + "Invalid Arguments. /pkt [join | start | end]");
            } else if(args[0].equalsIgnoreCase("join")) {
                try {
                    PTUtils.joinGame(p, prefix);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if(args[0].equalsIgnoreCase("qlist")) {
                PTUtils.listPlayers(prefix, p);
            } else if(args[0].equalsIgnoreCase("start")) {
                try {
                    PTUtils.startGame(prefix);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Logger logger = Bukkit.getLogger();
            logger.warning("You must run this command in-game!");
        }

        return true;
    }
}
