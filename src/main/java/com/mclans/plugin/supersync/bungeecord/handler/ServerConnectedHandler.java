package com.mclans.plugin.supersync.bungeecord.handler;

import com.mclans.plugin.supersync.bungeecord.SuperSyncBC;
import com.mclans.plugin.supersync.entity.PackData;
import com.mclans.plugin.supersync.entity.PlayerData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerConnectedHandler implements Listener {
    PlayerData cache;
    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        cache = new PlayerData(
                player.getUniqueId(),
                player.getName(),
                event.getServer().getInfo().getName()
        );
        if(SuperSyncBC.playerdatamap.containsKey(player.getUniqueId())) {
            cache = SuperSyncBC.playerdatamap.get(player.getUniqueId());
            cache.setServerName(event.getServer().getInfo().getName());
        } else {
            cache.loadFromDB();
            SuperSyncBC.playerdatamap.put(player.getUniqueId(), cache);
        }
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        try {
            ObjectOutputStream obj = new ObjectOutputStream(bytes);
            PlayerData playerdata = cache;
            PackData pack = new PackData("UpdatePlayerData", playerdata);
            obj.writeObject(pack);
            event.getServer().sendData("SuperSync", bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
