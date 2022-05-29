package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class EndGame {

    public static void GameEnd() throws InterruptedException {

        inProgress = false;

        joinedPlayers.forEach((String playerName) -> {
            if (alivePlayers.size() > 0) {
                Player p = Bukkit.getPlayerExact(playerName);
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                p.sendMessage(prefix + ChatColor.WHITE + "Game Over!");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                p.sendMessage(prefix + ChatColor.WHITE + "Runners win!");
                try {
                    TimeUnit.SECONDS.sleep((long) 1.5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                p.sendMessage(prefix + ChatColor.WHITE + "Returning to lobby!");
            } else {
                Player p = Bukkit.getPlayerExact(playerName);
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                p.sendMessage(prefix + ChatColor.WHITE + "Game Over!");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                p.sendMessage(prefix + ChatColor.WHITE + "The Hunter wins!");
                try {
                    TimeUnit.SECONDS.sleep((long) 1.5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                p.sendMessage(prefix + ChatColor.WHITE + "Returning to lobby!");
                //Location lobby = new Location(0, 0, 0);
                //p.teleport(lobby);
            }
        });

        joinedPlayers.clear();
        alivePlayers.clear();
        roles.clear();
    }
}
