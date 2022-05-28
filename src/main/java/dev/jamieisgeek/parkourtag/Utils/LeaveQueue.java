package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.entity.Player;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.alivePlayers;
import static dev.jamieisgeek.parkourtag.Utils.PTUtils.joinedPlayers;

public class LeaveQueue {

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
}
