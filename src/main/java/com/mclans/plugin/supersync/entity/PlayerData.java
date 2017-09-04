package com.mclans.plugin.supersync.entity;

import com.mclans.plugin.supersync.bungeecord.SuperSyncBC;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData implements Serializable {
    private String playername;
    private UUID uuid;
    private double health;
    private double maxhealth;
    private int foodlevel;
    private boolean isupdated;
    private String servername;
    private String targetservername;
//    private ConcurrentHashMap<Integer, String> itemmap;
    private String itemstr;
    public PlayerData(UUID uuid, String name, String info) {
        this.uuid = uuid;
        this.playername = name;
        this.servername = info;
        this.isupdated = false;
    }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMaxHealth() {
        return this.maxhealth;
    }

    public void setMaxHealth(double maxhealth) {
        this.maxhealth = maxhealth;
    }

    public int getFoodlevel() {
        return this.foodlevel;
    }

    public void setFoodlevel(int foodlevel) {
        this.foodlevel = foodlevel;
    }

    public String getServerName() {
        return servername;
    }
    public void setServerName(String name) {
        this.servername = name;
    }
    public void setIsUpdated(boolean bool) {
        this.isupdated = bool;
    }
    public boolean isUpdated() {
        return isupdated;
    }

    public void setTargetServerName(String info) {
        this.targetservername = info;
    }

    public String getTargetServerName() {
        return this.targetservername;
    }

    public UUID getUUID() {
        return uuid;
    }
    public void setItemStr(String itemstr) {
        this.itemstr = itemstr;
    }
    public String getItemStr() {
        return itemstr;
    }
    public void saveToDB() {
        //save all fields to database
        SuperSyncBC.log.info(uuid.toString());
        String sql = "SELECT `uuid` FROM `supersync` WHERE `uuid`='" + uuid.toString() + "'";
        HashMap list = SuperSyncBC.pool.query(sql);
        if(list.containsKey(uuid.toString())) {
            sql = "UPDATE `supersync` SET `health`=" + String.valueOf((int)health) + ","
                    + " `maxhealth`=" + String.valueOf((int)maxhealth) + ","
                    + " `foodlevel`=" + String.valueOf(foodlevel) + ","
                    + " `itemstr`='" + itemstr
                    + "' WHERE `uuid`='" + uuid.toString() + "'";
        } else {
            sql = "INSERT INTO `supersync` (`uuid`, `username`, `health`, `maxhealth`, `foodlevel`, `itemstr`) VALUES " +
                    "('" + uuid.toString() + "', '" +
                    playername + "', " +
                    String.valueOf(health) + ", " +
                    String.valueOf(maxhealth) + ", " +
                    String.valueOf(foodlevel) + ", '" +
                    itemstr + "')";
        }
//        SuperSyncBC.log.info(sql);
        SuperSyncBC.pool.execute(sql);
    }
    public void loadFromDB() {
        // set all fields
        String sql = "SELECT * FROM `supersync` WHERE `uuid`='" + uuid.toString() + "'";
        HashMap list = SuperSyncBC.pool.query(sql);
        if(list.containsKey(uuid.toString())) {
            HashMap map = (HashMap) list.get(uuid.toString());
            playername = map.get("username").toString();
            health = (double) map.get("health");
            maxhealth = (double) map.get("maxhealth");
            foodlevel = (int) map.get("foodlevel");
            itemstr = map.get("itemstr").toString();
        }
    }

    public void saveCache(PlayerData data) {
        if (data.getHealth() > 0) {
            this.health = data.getHealth();
            SuperSyncBC.log.info("saveCache(): health = " + String.valueOf(health));
        }
        if (data.getMaxHealth() > 0) {
            this.maxhealth = data.getMaxHealth();
            SuperSyncBC.log.info("saveCache(): maxhealth = " + String.valueOf(maxhealth));
        }
        if (data.getFoodlevel() > 0) {
            this.foodlevel = data.getFoodlevel();
            SuperSyncBC.log.info("saveCache(): foodlevel = " + String.valueOf(foodlevel));
        }
        if (data.getItemStr() != null) {
            this.itemstr = data.getItemStr();
            SuperSyncBC.log.info("saveCache(): itemstr = " + itemstr);
        }
        this.isupdated = true;
    }
}
