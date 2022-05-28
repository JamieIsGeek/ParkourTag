package dev.jamieisgeek.parkourtag.Events;

import dev.jamieisgeek.parkourtag.Utils.EndGame;
import dev.jamieisgeek.parkourtag.Utils.Fireworks;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.EventListener;

import static dev.jamieisgeek.parkourtag.Utils.EndGame.GameEnd;
import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class Events implements Listener {

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
                    EndGame.GameEnd();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) throws InterruptedException {
        e.setCancelled(true);

        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player attacker = (Player) e.getDamager();

            if(e.getEntity().getType() == EntityType.PLAYER) {
                Player attacked = (Player) e.getEntity();

                if(attacker.getDisplayName().equalsIgnoreCase(roles.get("Hunter"))) {
                    attacked.setGameMode(GameMode.SPECTATOR);
                    alivePlayers.remove(attacked.getDisplayName());
                    Location deathLoc = attacked.getLocation();


                    FireworkEffect fireworkEffect = FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BALL).withColor(Color.ORANGE).withFade(Color.RED).build();
                    Location location = attacked.getLocation();
                    new Fireworks().InstantFirework(fireworkEffect, location);
                    joinedPlayers.forEach((String playerName) -> {
                        Player p = Bukkit.getPlayerExact(playerName);
                        p.sendMessage(prefix + ChatColor.RED + attacked.getDisplayName() + ChatColor.WHITE + " has been " + ChatColor.BOLD + "" + ChatColor.RED + "eliminated" + ChatColor.stripColor("") + ChatColor.WHITE + "!");
                    });

                    if(alivePlayers.size() == 0) {
                        EndGame.GameEnd();
                    }
                }
            }
        }
    }
}
