package dev.jamieisgeek.parkourtag.Events;

import dev.jamieisgeek.parkourtag.Utils.EndGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.*;

public class Events implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) throws InterruptedException {
        if(joinedPlayers.contains(e.getPlayer().getUniqueId())) {
            joinedPlayers.remove(e.getPlayer().getUniqueId());
            if(alivePlayers.contains(e.getPlayer().getUniqueId())) {
                alivePlayers.remove(e.getPlayer().getUniqueId());

                joinedPlayers.forEach((UUID id) -> {
                    Player p = Bukkit.getPlayer(id);
                    p.sendMessage(prefix + e.getPlayer().getDisplayName() + " has left the game!");
                });

                if(String.valueOf(e.getPlayer().getUniqueId()) == roles.get("Hunter")) {
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

                if(String.valueOf(attacker.getUniqueId()).equalsIgnoreCase(roles.get("Hunter"))) {
                    attacked.setGameMode(GameMode.SPECTATOR);
                    alivePlayers.remove(attacked.getUniqueId());
                    Location deathLoc = attacked.getLocation();

                    joinedPlayers.forEach((UUID id) -> {
                        Player p = Bukkit.getPlayer(id);
                        p.sendMessage(prefix + ChatColor.RED + attacked.getName() + ChatColor.WHITE + " has been " + ChatColor.BOLD + "" + ChatColor.RED + "eliminated" + ChatColor.stripColor("") + ChatColor.WHITE + "!");
                    });

                    if(alivePlayers.size() == 0) {
                        EndGame.GameEnd();
                    }
                }
            }
        }
    }
}
