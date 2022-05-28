package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class GameInProgress {

    public static void GameInProgress() {
        final int[] startTimer = {6};
        int players = joinedPlayers.size() - 1;

        new BukkitRunnable() {
            @Override
            public void run() {
                startTimer[0] = startTimer[0] - 1;
                joinedPlayers.forEach((String playerName) -> {
                    Player p = Bukkit.getPlayerExact(playerName);
                    p.sendMessage(prefix + ChatColor.WHITE + "Game starts in: " + startTimer[0]);
                    if(startTimer[0] == 1) {
                        this.cancel();
                        p.sendMessage(prefix + "Run!");
                    }
                });
            }
        }.runTaskTimer(main, 0, 20);

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

                joinedPlayers.forEach((String playerName) -> {
                    Player p = Bukkit.getPlayerExact(playerName);
                    if (p.getDisplayName().equals(roles.get("Hunter"))) {
                        ScoreboardManager manager = Bukkit.getScoreboardManager();
                        Scoreboard scoreboard = manager.getNewScoreboard();
                        Objective objective = scoreboard.registerNewObjective("main", "dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                        Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + roles.get("Hunter"));
                        Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                        Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Hunter");
                        Score emptyScore = objective.getScore("");
                        Score timerScore = objective.getScore(ChatColor.RED + "Round Time: " + ChatColor.WHITE + PTUtils.shortInteger(duration - 1));

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

                        Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + roles.get("Hunter"));
                        Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                        Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Runner");
                        Score emptyScore = objective.getScore("");
                        Score timerScore = objective.getScore(ChatColor.RED + "Round Time: " + ChatColor.WHITE + PTUtils.shortInteger(duration - 1));

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
