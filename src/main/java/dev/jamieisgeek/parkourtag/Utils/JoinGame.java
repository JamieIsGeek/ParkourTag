package dev.jamieisgeek.parkourtag.Utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class JoinGame {

    public static void joinGame(Player p, String prefix) throws InterruptedException {
        if(alivePlayers.size() > 1) {
            p.sendMessage(prefix + "There is already a game in progress. Please wait for this game to end!");
            return;
        }
        if(joinedPlayers.isEmpty()) {
            joinedPlayers.add(p.getDisplayName());
            alivePlayers.add(p.getDisplayName());
            p.sendMessage(prefix + ChatColor.WHITE + "Joined the queue!");

        } else {
            if(joinedPlayers.contains(p.getDisplayName())) {
                p.sendMessage(prefix + ChatColor.WHITE + "You are already in the queue for this game!");
            } else {
                joinedPlayers.add(p.getDisplayName());
                alivePlayers.add(p.getDisplayName());
                p.sendMessage(prefix + ChatColor.WHITE + "Joined the queue!");

                if(joinedPlayers.size() >= 3) {
                    joinedPlayers.forEach((String playerName) -> {
                        Player player = Bukkit.getPlayerExact(playerName);
                        player.sendMessage(prefix + "The game will start momentarily");
                    });

                    BeginGame.startGame();
                }
            }
        }

    }
}
