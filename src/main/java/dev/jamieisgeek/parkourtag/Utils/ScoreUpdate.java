package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.UUID;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class ScoreUpdate {

    public static int duration = 300;

    public static void updateScoreboard() {
        int players = joinedPlayers.size() - 1;
        new BukkitRunnable() {

            @Override
            public void run() {

                duration = duration - 1;

                if(duration == 0) {

                    try {
                        EndGame.GameEnd();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(!inProgress) {
                    this.cancel();
                }

                joinedPlayers.forEach((UUID id) -> {
                    Player p = Bukkit.getPlayer(id);
                    if (String.valueOf(p.getUniqueId()).equals(String.valueOf(hunter.getUniqueId()))) {
                        ScoreboardManager manager = Bukkit.getScoreboardManager();
                        Scoreboard scoreboard = manager.getNewScoreboard();
                        Objective objective = scoreboard.registerNewObjective("main", "dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                        Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getName());
                        Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                        Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Hunter");
                        Score emptyScore = objective.getScore("");
                        Score timerScore = objective.getScore(ChatColor.RED + "Round Time: " + ChatColor.WHITE + PTUtils.shortInteger(duration));

                        roleScore.setScore(5);
                        hunterScore.setScore(4);
                        inGameScore.setScore(3);
                        emptyScore.setScore(2);
                        timerScore.setScore(1);

                        p.setScoreboard(scoreboard);
                    } else {
                        ScoreboardManager manager = Bukkit.getScoreboardManager();
                        Scoreboard scoreboard = manager.getNewScoreboard();
                        Objective objective = scoreboard.registerNewObjective("main", "dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                        Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getName());
                        Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                        Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Runner");
                        Score emptyScore = objective.getScore("");
                        Score timerScore = objective.getScore(ChatColor.RED + "Round Time: " + ChatColor.WHITE + PTUtils.shortInteger(duration));

                        roleScore.setScore(5);
                        hunterScore.setScore(4);
                        inGameScore.setScore(3);
                        emptyScore.setScore(2);
                        timerScore.setScore(1);

                        p.setScoreboard(scoreboard);
                    }
                });
            }
        }.runTaskTimer(main, 0, 20);
    }
}
