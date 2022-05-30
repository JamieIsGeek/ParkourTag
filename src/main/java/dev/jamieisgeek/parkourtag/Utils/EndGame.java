package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class EndGame {

    public static void GameEnd() throws InterruptedException {

        inProgress = false;

        joinedPlayers.forEach((UUID id) -> {
            if (alivePlayers.size() > 0) {
                Player p = Bukkit.getPlayer(id);
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
                Player p = Bukkit.getPlayer(id);
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
            }
        });

        TimeUnit.SECONDS.sleep((long) 1.5);
        joinedPlayers.clear();
        TimeUnit.SECONDS.sleep((long) 0.6);
        alivePlayers.clear();
        TimeUnit.SECONDS.sleep((long) 0.6);
        roles.clear();
    }
}
