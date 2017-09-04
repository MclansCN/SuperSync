package com.mclans.plugin.supersync.bungeecord;

import com.mclans.plugin.supersync.bungeecord.handler.*;
import com.mclans.plugin.supersync.bungeecord.util.DBConnctionPool;
import com.mclans.plugin.supersync.entity.PlayerData;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SuperSyncBC extends Plugin {
    public static ConcurrentHashMap<UUID, PlayerData> playerdatamap;
    public static Logger log;
    public static SuperSyncBC instance;
    public static DBConnctionPool pool;
    private Configuration configuration;
    private int version;

    @Override
    public void onEnable() {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), "bconfig.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("bconfig.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "bconfig.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log = getLogger();
        pool = new DBConnctionPool(
                configuration.getString("Host"),
                configuration.getString("Port"),
                configuration.getString("Database"),
                configuration.getString("Username"),
                configuration.getString("Password"),
                configuration.getInt("Thread")
        );
        pool.open();
        String initSql = "CREATE TABLE IF NOT EXISTS `supersync` (\n" +
                "  `uuid` varchar(255) NOT NULL,\n" +
                "  `username` varchar(16) NOT NULL,\n" +
                "  `foodlevel` int(11) NOT NULL DEFAULT '-1',\n" +
                "  `health` double NOT NULL DEFAULT '-1',\n" +
                "  `maxhealth` double NOT NULL DEFAULT '-1',\n" +
                "  `itemstr` longtext,\n" +
                "  PRIMARY KEY (`uuid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        pool.execute(initSql);
        instance = this;
        log.info("Yay! It loads!");
        playerdatamap = new ConcurrentHashMap<>();
        getProxy().registerChannel("SuperSync");
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectHandler());
        getProxy().getPluginManager().registerListener(this, new PluginReceiveHandler());
        getProxy().getPluginManager().registerListener(this, new ServerConnectedHandler());
        getProxy().getPluginManager().registerListener(this, new ServerConnectHandler());
    }

    public static SuperSyncBC getInstance() {
        return instance;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
}
