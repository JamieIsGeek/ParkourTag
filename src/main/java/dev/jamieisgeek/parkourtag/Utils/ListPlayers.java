package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.entity.Player;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.joinedPlayers;

public class ListPlayers {

    public static void listPlayers(String prefix, Player p) {
        String playersJoined1 = joinedPlayers.toString().replace("[", "");
        String playersJoined2 = playersJoined1.replace("]", "");
        String playersJoined = playersJoined2.replace(" ", ", ");

        p.sendMessage(prefix + "There are: " + joinedPlayers.size() + " players in the queue!");
        p.sendMessage(prefix + playersJoined);
    }
}
