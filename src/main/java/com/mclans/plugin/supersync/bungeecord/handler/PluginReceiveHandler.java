package com.mclans.plugin.supersync.bungeecord.handler;

import com.mclans.plugin.supersync.bungeecord.SuperSyncBC;
import com.mclans.plugin.supersync.entity.PackData;
import com.mclans.plugin.supersync.entity.PlayerData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PluginReceiveHandler implements Listener {
    @EventHandler
    public void onPluginReceive(PluginMessageEvent event) {
        if (!event.getTag().equals("SuperSync")) return;
        ByteArrayInputStream data = new ByteArrayInputStream(event.getData());
        try {
            ObjectInputStream stream = new ObjectInputStream(data);
            PackData pack = (PackData) stream.readObject();
            SuperSyncBC.log.info("PluginMessageReceived: " + pack.getPackName());
            PlayerData playerdata;
            PlayerData cache;
            switch(pack.getPackName()) {
                case "SwitchServer":
                    playerdata = (PlayerData) pack.getBody();
                    cache = SuperSyncBC.playerdatamap.get(playerdata.getUUID());
                    cache.saveCache(playerdata);
                    cache.setIsUpdated(true);
                    SuperSyncBC.playerdatamap.put(cache.getUUID(), cache);
                    ProxiedPlayer player = SuperSyncBC.getInstance().getProxy().getPlayer(playerdata.getUUID());
                    player.connect(SuperSyncBC.getInstance().getProxy().getServerInfo(cache.getTargetServerName()));
                    break;
                case "QuitServer":
                    playerdata = (PlayerData) pack.getBody();
                    cache = SuperSyncBC.playerdatamap.get(playerdata.getUUID());
                    if(!cache.isUpdated()) {
                        cache.saveCache(playerdata);
                    }
                    cache.setIsUpdated(false);
                    SuperSyncBC.log.info("set Update = false");
                    SuperSyncBC.playerdatamap.put(cache.getUUID(), cache);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
