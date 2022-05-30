package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class GameInProgress {

    public static void GameInProgress() {
        final int[] startTimer = {6};

        new BukkitRunnable() {
            @Override
            public void run() {

                startTimer[0] = startTimer[0] - 1;
                joinedPlayers.forEach((UUID id) -> {
                    Player p = Bukkit.getPlayer(id);
                    p.sendMessage(prefix + ChatColor.WHITE + "Game starts in: " + startTimer[0]);
                    if(startTimer[0] == 1) {
                        p.sendMessage(prefix + "Run!");
                        this.cancel();
                        inProgress = true;
                        ScoreUpdate.updateScoreboard();
                    }
                });
            }
        }.runTaskTimer(main, 0, 20);
    }
}
