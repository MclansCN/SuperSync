package com.mclans.plugin.supersync.bungeecord.handler;

import com.mclans.plugin.supersync.bungeecord.SuperSyncBC;
import com.mclans.plugin.supersync.entity.PlayerData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectHandler implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if(SuperSyncBC.playerdatamap.containsKey(event.getPlayer().getUniqueId())) {
            PlayerData cache = SuperSyncBC.playerdatamap.get(event.getPlayer().getUniqueId());
            cache.saveToDB();
            SuperSyncBC.playerdatamap.remove(event.getPlayer().getUniqueId());
        }
    }
}
