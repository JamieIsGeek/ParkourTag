package dev.jamieisgeek.parkourtag.Commands;

import dev.jamieisgeek.parkourtag.ParkourTag;
import dev.jamieisgeek.parkourtag.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;


public class ParkourTagCommand implements CommandExecutor {
    public static Plugin main = ParkourTag.getProvidingPlugin(ParkourTag.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;
            String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "ParkourTag" + ChatColor.RESET + ChatColor.WHITE + "] ";

            if(args.length != 1) {
                p.sendMessage(prefix + ChatColor.WHITE + "Invalid Arguments. /pkt [join | start | end | qleave | qlist | setspawn | setlobby | reload]");
            } else if(args[0].equalsIgnoreCase("join")) {
                try {
                    JoinGame.joinGame(p, prefix);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if(args[0].equalsIgnoreCase("qlist")) {
                if(p.hasPermission("pkt.qlist")) {
                    ListPlayers.listPlayers(prefix, p);
                } else {
                    p.sendMessage(prefix + "Missing Permission: pkt.qlist");
                }

            } else if(args[0].equalsIgnoreCase("start")) {
                if(p.hasPermission("pkt.forcestart")) {
                    try {
                        BeginGame.startGame();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    p.sendMessage(prefix + "Missing Permission: pkt.forcestart");
                }

            } else if(args[0].equalsIgnoreCase("end")) {
                if(p.hasPermission("pkt.forceend")) {
                    try {
                        EndGame.GameEnd();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    p.sendMessage(prefix + "Missing Permission: pkt.forceend");
                }

            } else if(args[0].equalsIgnoreCase("qleave")) {
                if(p.hasPermission("pkt.qleave")) {
                    LeaveQueue.leaveQueue(prefix, p);
                } else {
                    p.sendMessage(prefix + "Missing Permission: pkt.qleave");
                }
            } else if(args[0].equalsIgnoreCase("setarena")) {
                if(p.hasPermission("pkt.setarena")) {
                } else {
                    p.sendMessage(prefix + "Missing Permission: pkt.setarena");
                }
            } else if(args[0].equalsIgnoreCase("setlobby")) {
                if(p.hasPermission("pkt.setlobby")) {

                } else {
                    p.sendMessage(prefix + "Missing Permission: pkt.setlobby");
                }
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(p.hasPermission("pkt.reload")) {
                    main.reloadConfig();
                    main.getConfig();
                    p.sendMessage(prefix + "Plugin config has been reloaded successfully and all changes have been applied!");
                }
            }


        } else {
            Logger logger = Bukkit.getLogger();
            logger.warning("You must run this command in-game!");
        }

        return true;
    }
}
