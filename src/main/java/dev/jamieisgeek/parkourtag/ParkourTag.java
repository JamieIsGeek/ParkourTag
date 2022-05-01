package dev.jamieisgeek.parkourtag;

import dev.jamieisgeek.parkourtag.Commands.ParkourTagCommand;
import dev.jamieisgeek.parkourtag.Managers.GameState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ParkourTag extends JavaPlugin {

    Logger logger = Bukkit.getLogger();
    public GameState gameState = GameState.LOBBY;

    @Override
    public void onEnable() {
        getCommand("parkourtag").setExecutor(new ParkourTagCommand());


        logger.info("=+=+=+=+=+=+=+=");
        logger.info("ParkourTag has Enabled!");
        logger.info("Version: 1.0");
        logger.info("=+=+=+=+=+=+=+=");

    }

    @Override
    public void onDisable() {
        logger.info("=+=+=+=+=+=+=+=");
        logger.info("ParkourTag has Disabled!");
        logger.info("Version: 1.0");
        logger.info("=+=+=+=+=+=+=+=");
    }
}
