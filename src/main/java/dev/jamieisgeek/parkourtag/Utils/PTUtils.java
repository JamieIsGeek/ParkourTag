package dev.jamieisgeek.parkourtag.Utils;

import dev.jamieisgeek.parkourtag.ParkourTag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
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
}