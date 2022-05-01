package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PTUtils {

    public static ArrayList<String> joinedPlayers = new ArrayList<>();

    public static void joinGame(Player p, String prefix) throws InterruptedException {

        if(joinedPlayers.isEmpty()) {
            joinedPlayers.add(p.getDisplayName());
            p.sendMessage(prefix + ChatColor.WHITE + "Joined the queue!");

            if(joinedPlayers.size() > 3) {
                TimeUnit.SECONDS.sleep(5);
                PTUtils.startGame(prefix);
            }
        } else {
            if(joinedPlayers.contains(p.getDisplayName())) {
                p.sendMessage(prefix + ChatColor.WHITE + "You are already in the queue for this game!");
            } else {
                joinedPlayers.add(p.getDisplayName());
                p.sendMessage(prefix + ChatColor.WHITE + "Joined the queue!");

                if(joinedPlayers.size() > 3) {
                    TimeUnit.SECONDS.sleep(60);
                    PTUtils.startGame(prefix);
                }
            }
        }

    }

    public static void startGame(String prefix) throws InterruptedException {
        /*
        Teleport players to the arena - Don't start until 5 seconds after the hunter has been chosen!
        */

        // Assigns the "Hunter" role to a random person from the players that have joined the game
        int random = new Random().nextInt(joinedPlayers.size());
        String hunterBefore = joinedPlayers.get(random);
        Player hunter = Bukkit.getPlayerExact(hunterBefore);

        HashMap<String, String> roles = new HashMap<>();

        for(int i = 0; i < joinedPlayers.size(); i++) {
            if(joinedPlayers.get(i).equals(hunter.getDisplayName())) {
                roles.put("Hunter", hunter.getDisplayName());
            } else {
                roles.put(joinedPlayers.get(i), "Runner");
            }
        }


        // Create the scoreboard!

        for(int i = 0; i < joinedPlayers.size(); i++) {

            Player p = Bukkit.getPlayerExact(joinedPlayers.get(i));

            if(p.getDisplayName().equals(roles.get("Hunter"))) {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = manager.getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("main","dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
                Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + joinedPlayers.size());
                Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Hunter");

                roleScore.setScore(2);
                hunterScore.setScore(1);
                inGameScore.setScore(0);

                p.setScoreboard(scoreboard);
            } else {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = manager.getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("main","dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
                Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + joinedPlayers.size());
                Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Runner");

                roleScore.setScore(2);
                hunterScore.setScore(1);
                inGameScore.setScore(0);

                p.setScoreboard(scoreboard);
            }
        }

        TimeUnit.SECONDS.sleep(5);
        for(int i = 0; i < joinedPlayers.size(); i++) {
            String playerName = joinedPlayers.get(i);
            Player p = Bukkit.getPlayerExact(playerName);

            p.sendMessage(prefix + ChatColor.WHITE + "Start!");
        }
    }
}
