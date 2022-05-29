package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class BeginGame {

    public static void startGame() throws InterruptedException {
        if(joinedPlayers.size() > 1) {
            joinedPlayers.forEach((String playerName) -> {
                Player player = Bukkit.getPlayerExact(playerName);
                player.sendMessage(prefix + "Teleporting...");
                //player.teleport();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //player.teleport();
            });

            int random = new Random().nextInt(joinedPlayers.size());
            String hunterBefore = joinedPlayers.get(random);
            Player hunter = Bukkit.getPlayerExact(hunterBefore);
            alivePlayers.remove(hunterBefore);

            joinedPlayers.forEach((String playerName) -> {
                if(playerName.equals(hunter.getDisplayName())) {
                    roles.put("Hunter", hunter.getDisplayName());
                } else {
                    roles.put(playerName, "Runner");
                }
            });

            // Create the scoreboard!
            int players = joinedPlayers.size() - 1;

            joinedPlayers.forEach((String playerName) -> {
                Player p = Bukkit.getPlayerExact(playerName);

                if (p.getDisplayName().equals(roles.get("Hunter"))) {
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    Scoreboard scoreboard = manager.getNewScoreboard();
                    Objective objective = scoreboard.registerNewObjective("main", "dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
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

                    Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
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

            joinedPlayers.forEach((String playerName) -> {
                Player p = Bukkit.getPlayerExact(playerName);
                p.sendMessage(prefix + "Welcome to Parkour Tag!\n" + prefix + "This gamemode is simple, run from the hunter while doing parkour!\n" + prefix + "A random player has been declared as the 'Hunter' you must run away from them and not get hit!\n");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                p.sendMessage(prefix + "The game will start shortly");
            });

            TimeUnit.SECONDS.sleep(2);
            GameInProgress.GameInProgress();
        }
    }
}
