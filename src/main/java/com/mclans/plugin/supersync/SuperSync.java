package com.mclans.plugin.supersync;


import com.mclans.plugin.supersync.handler.PlayerDataLockHandler;
import com.mclans.plugin.supersync.handler.PlayerJoinHandler;
import com.mclans.plugin.supersync.handler.PlayerQuitHandler;
import com.mclans.plugin.supersync.handler.PluginMessageHandler;
import com.mclans.plugin.supersync.nms.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SuperSync extends JavaPlugin {
    public static Logger log;
    private static ItemStackSerializer itemSerializer;
    private static SuperSync instance;
    public static ConcurrentHashMap<UUID, Boolean> lockmap;
    @Override
    public void onEnable() {
        log = getLogger();
        instance = this;
        lockmap = new ConcurrentHashMap<>();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "SuperSync");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "SuperSync", new PluginMessageHandler());
        this.getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDataLockHandler(), this);
        getMcVersion();
    }

    public static SuperSync getInstance() {
        return instance;
    }

    private boolean getMcVersion() {
        String[] arrayOfString = Bukkit.getBukkitVersion().split("-");
        String str = arrayOfString[0];
        if ((str.matches("1.10.1")) || (str.matches("1.10.2"))) {
            log.info("Compatible server version detected: " + str);
            itemSerializer = new Version_1_10_R1();
            return true;
        }
        if ((str.matches("1.9.3")) || (str.matches("1.9.4"))) {
            log.info("Compatible server version detected: " + str);
            itemSerializer = new Version_1_9_R2();
            return true;
        }
        if ((str.matches("1.8.8")) || (str.matches("1.8.7")) || (str.matches("1.8.6")) || (str.matches("1.8.5")) || (str.matches("1.8.4"))) {
            log.info("Compatible server version detected: " + str);
            itemSerializer = new Version_1_8_R3();
            return true;
        }
        if (str.matches("1.7.10")) {
            log.info("Compatible server version detected: " + str);
            if (useKCauldron().booleanValue()) {
                log.info("KCauldron server detected. ProtocolLib dependency is required!");
                itemSerializer = new Version_1_7_R4_Kauldron();
                protocolLibInstalled();
                return true;
            }
            if (useCauldron().booleanValue()) {
                log.info("Cauldron server detected. ProtocolLib dependency is required!");
                itemSerializer = new Version_1_7_R4_Kauldron();
                protocolLibInstalled();
                return true;
            }
            itemSerializer = new Version_1_7_R4();
            return true;
        }

        log.warning("Incompatible server version found: " + str + " .Disabling Inventory, Armor and Enderchest Sync!");
        return false;
    }

    private static Boolean useKCauldron() {
        String str = "kcauldron.KCauldron";
        try {
            Class.forName(str);
            return Boolean.valueOf(true);
        } catch (Exception localException) {
        }
        return Boolean.valueOf(false);
    }

    private static Boolean useCauldron() {
        String str = "net.minecraftforge.cauldron.CauldronUtils";
        try {
            Class.forName(str);
            return Boolean.valueOf(true);
        } catch (Exception localException) {
        }
        return Boolean.valueOf(false);
    }

    private void protocolLibInstalled() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            log.info("ProtocolLib dependency found.");
        } else {
            log.severe("ProtocolLib dependency not found!!!");
            return;
        }
    }

    public static ItemStackSerializer getItemStackSerializer() {
        return itemSerializer;
    }
}
