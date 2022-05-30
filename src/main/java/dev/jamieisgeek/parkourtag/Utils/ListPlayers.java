package dev.jamieisgeek.parkourtag.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import static dev.jamieisgeek.parkourtag.Utils.PTUtils.joinedPlayers;

public class ListPlayers {

    public static void listPlayers(String prefix, Player p) {
        String playersJoined1 = joinedPlayers.toString().replace("[", "");
        String playersJoined2 = playersJoined1.replace("]", "");
        String playersJoined = playersJoined2.replace(" ", ", ");

        ArrayList<String> playerNames = new ArrayList<>();

        for(UUID id : joinedPlayers) {
            Player player = Bukkit.getPlayer(id);
            playerNames.add(player.getName());
        }

        p.sendMessage(prefix + "There are: " + joinedPlayers.size() + " players in the queue!");
        p.sendMessage(prefix + playerNames);
    }
}
