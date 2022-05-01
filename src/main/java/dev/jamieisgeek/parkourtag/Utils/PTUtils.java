package dev.jamieisgeek.parkourtag.Utils;

import dev.jamieisgeek.parkourtag.Managers.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PTUtils implements Listener {

    public static ArrayList<String> joinedPlayers = new ArrayList<>();
    public static ArrayList<String> alivePlayers = new ArrayList<>();
    public Player hunter;
    public HashMap<String, String> roles = new HashMap<>();
    public String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ParkourTag" + ChatColor.stripColor("") + ChatColor.WHITE + "] ";

    public static void joinGame(Player p, String prefix) throws InterruptedException {

        if(joinedPlayers.isEmpty()) {
            joinedPlayers.add(p.getDisplayName());
            alivePlayers.add(p.getDisplayName());
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
                alivePlayers.add(p.getDisplayName());
                p.sendMessage(prefix + ChatColor.WHITE + "Joined the queue!");

                if(joinedPlayers.size() > 3) {
                    TimeUnit.SECONDS.sleep(5);
                    GameState gameState = GameState.STARTING;
                    PTUtils.startGame(prefix);
                }
            }
        }

    }

    public static void listPlayers(String prefix, Player p) {
        p.sendMessage(prefix + ChatColor.WHITE + joinedPlayers.toString());
    }

    public static void startGame(String prefix) throws InterruptedException {
        if(joinedPlayers.size() > 1) {
            /*
            Teleport players to the arena - Don't start until 5 seconds after the hunter has been chosen!
            */

            // Assigns the "Hunter" role to a random person from the players that have joined the game
            int random = new Random().nextInt(joinedPlayers.size());
            String hunterBefore = joinedPlayers.get(random);
            Player hunter = Bukkit.getPlayerExact(hunterBefore);
            alivePlayers.remove(hunterBefore);

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

                TimeUnit.SECONDS.sleep(1);
                p.sendMessage(prefix + ChatColor.WHITE + "5");
                TimeUnit.SECONDS.sleep(1);
                p.sendMessage(prefix + ChatColor.WHITE + "4");
                TimeUnit.SECONDS.sleep(1);
                p.sendMessage(prefix + ChatColor.WHITE + "3");
                TimeUnit.SECONDS.sleep(1);
                p.sendMessage(prefix + ChatColor.WHITE + "2");
                TimeUnit.SECONDS.sleep(1);
                p.sendMessage(prefix + ChatColor.WHITE + "2");
                TimeUnit.SECONDS.sleep(1);
                p.sendMessage(prefix + ChatColor.WHITE + "1");
                TimeUnit.SECONDS.sleep(1);
                GameState gameState = GameState.ACTIVE;
                p.sendMessage(prefix + ChatColor.WHITE + "Start!");
            }
        } else {
            GameState gameState = GameState.LOBBY;
            System.out.println("There are not enough players to start the game!");
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player attacker = (Player) e.getDamager();

            if(e.getEntity().getType() == EntityType.PLAYER) {
                Player attacked = (Player) e.getEntity();

                if(attacker == Bukkit.getPlayerExact(roles.get("Hunter"))) {
                    e.setCancelled(true);

                    attacked.setGameMode(GameMode.SPECTATOR);
                    for(int i = 0; i < joinedPlayers.size(); i++) {
                        String playerName = joinedPlayers.get(i);
                        Player p = Bukkit.getPlayerExact(playerName);

                        p.sendMessage(prefix + ChatColor.RED + attacked.getDisplayName() + ChatColor.WHITE + " has been " + ChatColor.BOLD + "" + ChatColor.RED + "eliminated" + ChatColor.stripColor("") + ChatColor.WHITE + "!");
                    }
                }
            }
        }
    }
}
