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

    public static int duration = 300;

    public static String shortInteger(int duration) {

        String string = "";
        int minutes = 0;
        int seconds = 0;

        if (duration / 60 / 60 / 24 >= 1) {
            duration -= duration / 60 / 60 / 24 * 60 * 60 * 24;
        }

        if (duration / 60 >= 1) {
            minutes = duration / 60;
            duration -= duration / 60 * 60;
        }
        if (duration >= 1)
            seconds = duration;
        if (minutes <= 9) {
            string = String.valueOf(string) + "0" + minutes + ":";
        } else {
            string = String.valueOf(string) + minutes + ":";
        }
        if (seconds <= 9) {
            string = String.valueOf(string) + "0" + seconds;
        } else {
            string = String.valueOf(string) + seconds;
        }

        return string;
    }

    public static ArrayList<String> joinedPlayers = new ArrayList<>();
    public static ArrayList<String> alivePlayers = new ArrayList<>();
    public static Player hunter = null;
    public static HashMap<String, String> roles = new HashMap<>();
    public static String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ParkourTag" + ChatColor.RESET + ChatColor.WHITE + "] ";
    public static Plugin main = ParkourTag.getPlugin(ParkourTag.class);
    public static Location arena = main.getConfig().getLocation("spawn-location");
    public static Location lobby = main.getConfig().getLocation("lobby-location");
    public boolean inProgress = false;
    public static void joinGame(Player p, String prefix) throws InterruptedException {
        if(alivePlayers.size() > 1) {
            p.sendMessage(prefix + "There is already a game in progress. Please wait for this game to end!");
            return;
        }
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
                p.sendMessage(ChatColor.WHITE + "Welcome to Parkour Tag!\n" + prefix + "This gamemode is simple, run from the hunter while doing parkour!\n" + prefix + "A random player has been declared as the 'Hunter' you must run away from them and not get hit!\n");
            });

            TimeUnit.SECONDS.sleep(5);
            GameInProgress();
        }
    }

    public static void GameInProgress() {
        final int[] startTimer = {6};
        int players = joinedPlayers.size() - 1;

        joinedPlayers.forEach((String playerName) -> {
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

            new BukkitRunnable() {

                @Override
                public void run() {
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
                    duration = duration - 1;
                    if(duration == 0) {
                        try {
                            GameEnd();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }.runTaskTimer(main, 0, 20);
        });

    }



    public static void GameEnd() throws InterruptedException {

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
                p.teleport(lobby);
            }
        });

        joinedPlayers.clear();
        alivePlayers.clear();
    }

    public static void leaveQueue(String prefix, Player p) {
        if(joinedPlayers.contains(p.getDisplayName())) {
            joinedPlayers.remove(p.getDisplayName());

            if(alivePlayers.size() > 1) {
                p.sendMessage(prefix + "You are unable to leave during a game!");
            } else {
                p.sendMessage(prefix + "You left the queue!");
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) throws InterruptedException {
        if(joinedPlayers.contains(e.getPlayer().getDisplayName())) {
            joinedPlayers.remove(e.getPlayer().getDisplayName());
            if(alivePlayers.contains(e.getPlayer().getDisplayName())) {
                alivePlayers.remove(e.getPlayer().getDisplayName());

                joinedPlayers.forEach((String playerName) -> {
                    Player p = Bukkit.getPlayerExact(playerName);
                    p.sendMessage(prefix + e.getPlayer().getDisplayName() + " has left the game!");
                });

                if(e.getPlayer().getDisplayName() == roles.get("Hunter")) {
                    GameEnd();
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

                    joinedPlayers.forEach((String playerName) -> {
                        Player p = Bukkit.getPlayerExact(playerName);
                        p.sendMessage(prefix + ChatColor.RED + attacked.getDisplayName() + ChatColor.WHITE + " has been " + ChatColor.BOLD + "" + ChatColor.RED + "eliminated" + ChatColor.stripColor("") + ChatColor.WHITE + "!");
                    });
                }
            }
        }
    }
}