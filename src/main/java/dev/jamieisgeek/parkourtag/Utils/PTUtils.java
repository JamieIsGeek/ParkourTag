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
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PTUtils implements Listener {

    public static ArrayList<String> joinedPlayers = new ArrayList<>();
    public static ArrayList<String> alivePlayers = new ArrayList<>();
    public Player hunter = null;
    public static HashMap<String, String> roles = new HashMap<>();
    public static String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ParkourTag" + ChatColor.stripColor("") + ChatColor.WHITE + "] ";
    public static int timerLength = 300000;

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

            for(int i = 0; i < joinedPlayers.size(); i++) {
                if(joinedPlayers.get(i).equals(hunter.getDisplayName())) {
                    roles.put("Hunter", hunter.getDisplayName());
                } else {
                    roles.put(joinedPlayers.get(i), "Runner");
                }
            }

            Timer timer = new Timer(timerLength, null);

            // Create the scoreboard!
            int players = joinedPlayers.size() - 1;
            for(int i = 0; i < joinedPlayers.size(); i++) {

                Player p = Bukkit.getPlayerExact(joinedPlayers.get(i));

                if(p.getDisplayName().equals(roles.get("Hunter"))) {
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    Scoreboard scoreboard = manager.getNewScoreboard();
                    Objective objective = scoreboard.registerNewObjective("main","dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
                    Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                    Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Hunter");
                    //Score timerScore = objective.getScore(ChatColor.RED + "Time Remaining: " + ChatColor.WHITE + timer);
                    Score empty = objective.getScore("");
                    Score status = objective.getScore(ChatColor.RED + "Status: " + ChatColor.WHITE + "Alive");

                    roleScore.setScore(5);
                    hunterScore.setScore(4);
                    inGameScore.setScore(3);
                    //timerScore.setScore(2);
                    empty.setScore(1);
                    status.setScore(0);

                    p.setScoreboard(scoreboard);
                } else {
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    Scoreboard scoreboard = manager.getNewScoreboard();
                    Objective objective = scoreboard.registerNewObjective("main","dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
                    Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                    Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Runner");
                    //Score timerScore = objective.getScore(ChatColor.RED + "Time Remaining: " + ChatColor.WHITE + timer);
                    Score empty = objective.getScore("");
                    Score status = objective.getScore(ChatColor.RED + "Status: " + ChatColor.WHITE + "Alive");

                    roleScore.setScore(5);
                    hunterScore.setScore(4);
                    inGameScore.setScore(3);
                    //timerScore.setScore(2);
                    empty.setScore(1);
                    status.setScore(0);

                    p.setScoreboard(scoreboard);
                }
            }

            //TimeUnit.SECONDS.sleep(5);
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
                p.sendMessage(prefix + ChatColor.WHITE + "1");
                TimeUnit.SECONDS.sleep(1);
                GameState gameState = GameState.ACTIVE;
                p.sendMessage(prefix + ChatColor.WHITE + "Start!");
            }
            timer.setRepeats(false);
            timer.start();

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(() -> {
                if(alivePlayers.isEmpty()) {
                    try {
                        PTUtils.GameEnd();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(timer.isRunning() == false) {
                    try {
                        PTUtils.GameEnd();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000L, 1000L, TimeUnit.MILLISECONDS);

        } else {
            GameState gameState = GameState.LOBBY;
            System.out.println("There are not enough players to start the game!");
        }
    }

    public static void GameEnd() throws InterruptedException {
        for(int i = 0; i < joinedPlayers.size(); i++) {
            if(alivePlayers.size() > 0) {
                Player p = Bukkit.getPlayerExact(joinedPlayers.get(i));
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                p.sendMessage(prefix + ChatColor.WHITE + "Game Over!");
                TimeUnit.SECONDS.sleep(2);
                p.sendMessage(prefix + ChatColor.WHITE + "Runners win!");
                p.sendMessage(prefix + ChatColor.WHITE + "Returning to lobby!");
            } else {
                Player p = Bukkit.getPlayerExact(joinedPlayers.get(i));
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                p.sendMessage(prefix + ChatColor.WHITE + "Game Over!");
                TimeUnit.SECONDS.sleep(2);
                p.sendMessage(prefix + ChatColor.WHITE + "The Hunter wins!");
                TimeUnit.SECONDS.sleep(4);
                p.sendMessage(prefix + ChatColor.WHITE + "Returning to lobby!");
                // Teleport player back to the lobby!
            }
        }

        joinedPlayers.clear();
        alivePlayers.clear();
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        e.setCancelled(true);

        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player attacker = (Player) e.getDamager();

            if(e.getEntity().getType() == EntityType.PLAYER) {
                Player attacked = (Player) e.getEntity();

                if(attacker.getDisplayName().equalsIgnoreCase(roles.get("Hunter"))) {
                    attacked.setGameMode(GameMode.SPECTATOR);
                    alivePlayers.remove(attacked.getDisplayName());



                    for(int i = 0; i < joinedPlayers.size(); i++) {
                        String playerName = joinedPlayers.get(i);
                        Player p = Bukkit.getPlayerExact(playerName);
                        int players = joinedPlayers.size() - 1;

                        p.sendMessage(prefix + ChatColor.RED + attacked.getDisplayName() + ChatColor.WHITE + " has been " + ChatColor.BOLD + "" + ChatColor.RED + "eliminated" + ChatColor.stripColor("") + ChatColor.WHITE + "!");


                        if(p.getDisplayName().equalsIgnoreCase(roles.get("Hunter"))) {
                            ScoreboardManager manager = Bukkit.getScoreboardManager();
                            Scoreboard scoreboard = manager.getNewScoreboard();
                            Objective objective = scoreboard.registerNewObjective("main","dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                            Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
                            Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                            Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Hunter");
                            //Score timerScore = objective.getScore(ChatColor.RED + "Time Remaining: " + ChatColor.WHITE + timer);
                            Score empty = objective.getScore("");
                            Score status = objective.getScore(ChatColor.RED + "Status: " + ChatColor.WHITE + "Alive");

                            roleScore.setScore(5);
                            hunterScore.setScore(4);
                            inGameScore.setScore(3);
                            //timerScore.setScore(2);
                            empty.setScore(1);
                            status.setScore(0);

                            p.setScoreboard(scoreboard);
                        } else {
                            ScoreboardManager manager = Bukkit.getScoreboardManager();
                            Scoreboard scoreboard = manager.getNewScoreboard();
                            Objective objective = scoreboard.registerNewObjective("main","dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                            Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
                            Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                            Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Runner");
                            //Score timerScore = objective.getScore(ChatColor.RED + "Time Remaining: " + ChatColor.WHITE + timer);
                            Score empty = objective.getScore("");
                            Score status = objective.getScore(ChatColor.RED + "Status: " + ChatColor.WHITE + "Alive");

                            roleScore.setScore(5);
                            hunterScore.setScore(4);
                            inGameScore.setScore(3);
                            //timerScore.setScore(2);
                            empty.setScore(1);
                            status.setScore(0);

                            p.setScoreboard(scoreboard);
                        }
                    }
                }
            }
        }
    }
}