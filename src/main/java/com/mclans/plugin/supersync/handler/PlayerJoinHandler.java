package com.mclans.plugin.supersync.handler;

import com.mclans.plugin.supersync.SuperSync;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SuperSync.lockmap.put(event.getPlayer().getUniqueId(), true);
    }
}
