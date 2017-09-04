package com.mclans.plugin.supersync.handler;

import com.mclans.plugin.supersync.ItemStackSerializer;
import com.mclans.plugin.supersync.SuperSync;
import com.mclans.plugin.supersync.entity.PackData;
import com.mclans.plugin.supersync.entity.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class PluginMessageHandler implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream obj = new ObjectInputStream(stream);

            PackData pack = (PackData) obj.readObject();

            SuperSync.log.info("onPluginMessageReceived: " + pack.getPackName());
            switch (pack.getPackName()) {
                case "SavePlayerData":
                    player.closeInventory();
                    SuperSync.lockmap.put(player.getUniqueId(), true);
                    PlayerData localplayerdata = new PlayerData(player.getUniqueId(), player.getName(), "");
                    // set all fields of localdata
                    localplayerdata.setFoodlevel(player.getFoodLevel());
                    localplayerdata.setHealth(player.getHealth());
                    localplayerdata.setMaxHealth(player.getMaxHealth());
                    ItemStack[] playeritems = player.getInventory().getContents();
                    ItemStackSerializer is = SuperSync.getItemStackSerializer();
                    String itemstr = is.toBase64(playeritems);
                    localplayerdata.setItemStr(itemstr);
                    ByteArrayOutputStream outbytes=new ByteArrayOutputStream();
                    ObjectOutputStream outobj = null;
                    try {
                        outobj = new ObjectOutputStream(outbytes);
                        PackData outpack = new PackData("SwitchServer", localplayerdata);
                        outobj.writeObject(outpack);
                        player.sendPluginMessage(SuperSync.getInstance(), "SuperSync", outbytes.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case "UpdatePlayerData":
                    PlayerData playerdata = (PlayerData) pack.getBody();
                    if (playerdata.getMaxHealth() > 0) player.setMaxHealth(playerdata.getMaxHealth());
                    if (playerdata.getHealth() > 0) player.setHealth(playerdata.getHealth());
                    if (playerdata.getFoodlevel() > 0) player.setFoodLevel(playerdata.getFoodlevel());
                    if (playerdata.getItemStr() != null && !playerdata.getItemStr().equals("null")) {
                        try {
                            Inventory inventory = player.getInventory();
                            String str = playerdata.getItemStr();
                            ItemStack[] items = SuperSync.getItemStackSerializer().fromBase64(str);
                            inventory.setContents(items);
                        } catch (IllegalAccessException e) {e.printStackTrace();

                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    SuperSync.lockmap.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GREEN + "用户数据同步完毕！");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
