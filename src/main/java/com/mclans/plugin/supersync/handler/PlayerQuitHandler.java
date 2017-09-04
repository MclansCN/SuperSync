package com.mclans.plugin.supersync.handler;

import com.mclans.plugin.supersync.SuperSync;
import com.mclans.plugin.supersync.bungeecord.SuperSyncBC;
import com.mclans.plugin.supersync.entity.PackData;
import com.mclans.plugin.supersync.entity.PlayerData;
import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutCustomPayload;
import net.minecraft.util.io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.StandardMessenger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;

public class PlayerQuitHandler implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SuperSync.log.info("PlayerQuit: " + player.getName());
        player.closeInventory();
        SuperSync.lockmap.put(player.getUniqueId(), true);
        PlayerData localplayerdata = new PlayerData(player.getUniqueId(), player.getName(), "");
        // set all fields of localdata
        localplayerdata.setMaxHealth(player.getMaxHealth());
        localplayerdata.setHealth(player.getHealth());
        localplayerdata.setFoodlevel(player.getFoodLevel());
        try {
            localplayerdata.setItemStr(SuperSync.getItemStackSerializer().toBase64(player.getInventory().getContents()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outbytes=new ByteArrayOutputStream();
        ObjectOutputStream outobj = null;
        try {
            outobj = new ObjectOutputStream(outbytes);
            PackData outpack = new PackData("QuitServer", localplayerdata);
            outobj.writeObject(outpack);
            ///////if it cannot keep a communication connection,
            // use the code below to save the data which cannot be sent to bungeecord plugin cache
            // to local flat file//////////

            Collection players = event.getPlayer().getServer().getOnlinePlayers();
            if(players.size() == 0) {
                SuperSync.log.info("None online!!!!!!!!!!!!!!!!!");

            }

            ///////////////////////////////////////////
//            SuperSync.getInstance().getServer().sendPluginMessage(SuperSync.getInstance(), "SuperSync", outbytes.toByteArray());
            SuperSync.log.info("Send QuitServer DataPack");
//            player.sendPluginMessage(SuperSync.getInstance(), "SuperSync", outbytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        SuperSync.lockmap.remove(event.getPlayer().getUniqueId());
    }
}
