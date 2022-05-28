package dev.jamieisgeek.parkourtag;

import dev.jamieisgeek.parkourtag.Commands.ParkourTagCommand;
import dev.jamieisgeek.parkourtag.Events.Events;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ParkourTag extends JavaPlugin {

    Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {
        getCommand("parkourtag").setExecutor(new ParkourTagCommand());
        getServer().getPluginManager().registerEvents(new Events(), this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        logger.info("");
        logger.info("=+=+=+=+=+=+=+=+=+=+=+=+=+=");
        logger.info("ParkourTag has Enabled!");
        logger.info("Version: 1.0");
        logger.info("=+=+=+=+=+=+=+=+=+=+=+=+=+=");
        logger.info("");

    }

    @Override
    public void onDisable() {
        logger.info("");
        logger.info("=+=+=+=+=+=+=+=+=+=+=+=+=+=");
        logger.info("ParkourTag has Disabled!");
        logger.info("Version: 1.0");
        logger.info("=+=+=+=+=+=+=+=+=+=+=+=+=+=");
        logger.info("");

    }

}
