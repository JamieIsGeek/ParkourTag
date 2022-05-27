package dev.jamieisgeek.parkourtag.Utils;

import dev.jamieisgeek.parkourtag.ParkourTag;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
public class PTUtils implements Listener {

    public static ArrayList<String> joinedPlayers = new ArrayList<>();
    public static ArrayList<String> alivePlayers = new ArrayList<>();
    public Player hunter = null;
    public static HashMap<String, String> roles = new HashMap<>();
    public static String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ParkourTag" + ChatColor.RESET + ChatColor.WHITE + "] ";
    public static Plugin main = ParkourTag.getProvidingPlugin(ParkourTag.class);
    public static boolean inProgress = false;
    public static Location arena = main.getConfig().getLocation("spawn-location");
    public static void joinGame(Player p, String prefix) throws InterruptedException {

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

                    startGame();
                }
            }
        }

    }

    public static void listPlayers(String prefix, Player p) {
        String playersJoined1 = joinedPlayers.toString().replace("[", "");
        String playersJoined2 = playersJoined1.replace("]", "");
        String playersJoined = playersJoined2.replace(" ", ", ");

        p.sendMessage(prefix + "There are: " + joinedPlayers.size() + " players in the queue!");
        p.sendMessage(prefix + playersJoined);
    }

    public static void startGame() throws InterruptedException {
        if(joinedPlayers.size() > 1) {
            if(arena == null) {
                joinedPlayers.forEach((String playerName) -> {
                    Player player = Bukkit.getPlayerExact(playerName);
                    player.sendMessage(prefix + "There is no current arena spawn set!");
                });
            }

            joinedPlayers.forEach((String playerName) -> {
                Player player = Bukkit.getPlayerExact(playerName);
                player.sendMessage(prefix + "Teleporting...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                player.teleport(arena);
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
            int minutes = 5;
            int seconds = 00;
            String timer = minutes + ":" + seconds;

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
                    Score timerScore = objective.getScore(ChatColor.RED + "Round Time: " + ChatColor.WHITE + timer);

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
                    Score timerScore = objective.getScore(ChatColor.RED + "Round Time: " + ChatColor.WHITE + timer);

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
                p.sendMessage(ChatColor.WHITE + "Welcome to Parkour Tag!\n" + prefix + "This gamemode is simple, run from the hunter while doing parkour!\n" + prefix + "A random player has been declared as the 'Hunter' you must run away from them and not get hit!\n");
            });

            TimeUnit.SECONDS.sleep(5);
            GameInProgress(inProgress);
        }
    }

    public static void GameInProgress(boolean inProgress) {
        final int[] startTimer = {6};
        for(String playerName : joinedPlayers) {
            Player p = Bukkit.getPlayerExact(playerName);
            new BukkitRunnable() {
                @Override
                public void run() {
                    startTimer[0] = startTimer[0] - 1;
                    p.sendMessage(prefix + ChatColor.WHITE + "Game starts in: " + startTimer[0]);
                    if(startTimer[0] == 0) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(main, 0, 20);

            p.sendMessage(prefix + "RUN!");

        }
    }



    public static void GameEnd() throws InterruptedException {
        for (String joinedPlayer : joinedPlayers) {
            if (alivePlayers.size() > 0) {
                Player p = Bukkit.getPlayerExact(joinedPlayer);
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                p.sendMessage(prefix + ChatColor.WHITE + "Game Over!");
                TimeUnit.SECONDS.sleep(2);
                p.sendMessage(prefix + ChatColor.WHITE + "Runners win!");
                TimeUnit.SECONDS.sleep((long) 1.5);
                p.sendMessage(prefix + ChatColor.WHITE + "Returning to lobby!");
            } else {
                Player p = Bukkit.getPlayerExact(joinedPlayer);
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                p.sendMessage(prefix + ChatColor.WHITE + "Game Over!");
                TimeUnit.SECONDS.sleep(2);
                p.sendMessage(prefix + ChatColor.WHITE + "The Hunter wins!");
                TimeUnit.SECONDS.sleep((long) 1.5);
                p.sendMessage(prefix + ChatColor.WHITE + "Returning to lobby!");
                // Teleport player back to the lobby!
            }
        }

        joinedPlayers.clear();
        alivePlayers.clear();
    }

    public static void leaveQueue(String prefix, Player p) {
        if(joinedPlayers.contains(p.getDisplayName())) {
            joinedPlayers.remove(p.getDisplayName());

            if(alivePlayers.contains(p.getDisplayName())) {
                p.sendMessage(prefix + "You are unable to leave during a game!");
            } else {
                p.sendMessage(prefix + "You left the queue!");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(joinedPlayers.contains(e.getPlayer().getDisplayName())) {
            joinedPlayers.remove(e.getPlayer().getDisplayName());
            if(alivePlayers.contains(e.getPlayer().getDisplayName())) {
                alivePlayers.remove(e.getPlayer().getDisplayName());

                for (String joinedPlayer : joinedPlayers) {
                    Player p = Bukkit.getPlayerExact(joinedPlayer);

                    p.sendMessage(prefix + e.getPlayer().getDisplayName() + " has left the game!");
                }
            }
        }
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
                    Location deathLoc = attacked.getLocation();


                    Firework fw = (Firework) deathLoc.getWorld().spawnEntity(deathLoc, EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();
                    fwm.setPower(2);
                    fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
                    fw.setFireworkMeta(fwm);
                    fw.detonate();


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

                            roleScore.setScore(5);
                            hunterScore.setScore(4);
                            inGameScore.setScore(3);

                            p.setScoreboard(scoreboard);
                        } else {
                            ScoreboardManager manager = Bukkit.getScoreboardManager();
                            Scoreboard scoreboard = manager.getNewScoreboard();
                            Objective objective = scoreboard.registerNewObjective("main","dummy", ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Parkour Tag");
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                            Score hunterScore = objective.getScore(ChatColor.RED + "Hunter: " + ChatColor.WHITE + hunter.getDisplayName());
                            Score inGameScore = objective.getScore(ChatColor.RED + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + players);
                            Score roleScore = objective.getScore(ChatColor.RED + "Role: " + ChatColor.WHITE + "Runner");

                            roleScore.setScore(5);
                            hunterScore.setScore(4);
                            inGameScore.setScore(3);

                            p.setScoreboard(scoreboard);
                        }
                    }
                }
            }
        }
    }
}