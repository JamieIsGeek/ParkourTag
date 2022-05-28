package dev.jamieisgeek.parkourtag.Utils;

import dev.jamieisgeek.parkourtag.ParkourTag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PKTSetSpawn {

    public static Plugin main = ParkourTag.getPlugin(ParkourTag.class);

    public static void SetPKTSpawn(Player p, String prefix) {

        Location location = p.getLocation();
        main.getConfig().set("spawn-location", location);
        main.saveConfig();

        p.sendMessage(prefix + "Set game spawn to your location!");
    }

    public static void SetPKTLobby(Player p, String prefix) {

        Location location = p.getLocation();
        main.getConfig().set("lobby-location", location);
        main.saveConfig();

        p.sendMessage(prefix + "Set lobby spawn to your location!");
    }
}
