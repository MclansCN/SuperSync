package com.mclans.plugin.supersync.handler;

import com.mclans.plugin.supersync.SuperSync;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerDataLockHandler implements Listener {
    @EventHandler
    public void onInventoryChange(InventoryPickupItemEvent event) {

    }
    @EventHandler
    public void onInventoryChange(InventoryDragEvent event) {
        if(SuperSync.lockmap.containsKey(event.getWhoClicked().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getWhoClicked().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryChange(InventoryOpenEvent event) {
        if(SuperSync.lockmap.containsKey(event.getPlayer().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryChange(InventoryClickEvent event) {
        if(SuperSync.lockmap.containsKey(event.getWhoClicked().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getWhoClicked().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryChange(InventoryInteractEvent event) {
        if(SuperSync.lockmap.containsKey(event.getWhoClicked().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getWhoClicked().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryChange(PlayerPickupItemEvent event) {
        if(SuperSync.lockmap.containsKey(event.getPlayer().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryChange(PlayerPickupArrowEvent event) {
        if(SuperSync.lockmap.containsKey(event.getPlayer().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryChange(PlayerItemConsumeEvent event) {
        if(SuperSync.lockmap.containsKey(event.getPlayer().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryChange(PlayerDropItemEvent event) {
        if(SuperSync.lockmap.containsKey(event.getPlayer().getUniqueId())) {
            if(SuperSync.lockmap.get(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
