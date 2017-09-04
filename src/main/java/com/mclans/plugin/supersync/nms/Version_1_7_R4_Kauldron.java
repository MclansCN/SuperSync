/*    */ package com.mclans.plugin.supersync.nms;
/*    */ 
/*    */ import com.comphenix.protocol.utility.StreamSerializer;

/*    */ import java.io.IOException;

/*    */ import com.mclans.plugin.supersync.ItemStackSerializer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
/*    */ import net.minecraft.server.v1_7_R4.MinecraftServer;
/*    */ import net.minecraft.server.v1_7_R4.PlayerInteractManager;
/*    */ import net.minecraft.util.com.mojang.authlib.GameProfile;

/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
/*    */ import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class Version_1_7_R4_Kauldron
/*    */   implements ItemStackSerializer
/*    */ {
/*    */ 
/*    */   public String toBase64(ItemStack[] paramArrayOfItemStack) {
/* 31 */     StringBuilder localStringBuilder = new StringBuilder();
/* 32 */     for (int i = 0; i < paramArrayOfItemStack.length; i++)
/*    */     {
/* 34 */       if (i > 0) {
/* 35 */         localStringBuilder.append(";");
/*    */       }
/* 37 */       if ((paramArrayOfItemStack[i] == null) || (paramArrayOfItemStack[i].getType() == Material.AIR))
/*    */         continue;
/*    */       try {
/* 40 */         localStringBuilder.append(StreamSerializer.getDefault().serializeItemStack(paramArrayOfItemStack[i]));
/*    */       }
/*    */       catch (IOException localIOException)
/*    */       {
/* 44 */         localIOException.printStackTrace();
/*    */       }
/*    */     }
/*    */ 
/* 48 */     String str = localStringBuilder.toString();
/* 49 */     return str;
/*    */   }
/*    */ 
/*    */   public ItemStack[] fromBase64(String paramString) {
/* 53 */     String[] arrayOfString = paramString.split(";");
/* 54 */     ItemStack[] arrayOfItemStack = new ItemStack[arrayOfString.length];
/* 55 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 56 */       if (arrayOfString[i].equals(""))
/*    */         continue;
/*    */       try {
/* 59 */         arrayOfItemStack[i] = StreamSerializer.getDefault().deserializeItemStack(arrayOfString[i]);
/*    */       }
/*    */       catch (IOException localIOException)
/*    */       {
/* 63 */         localIOException.printStackTrace();
/*    */       }
/*    */     }
/*    */ 
/* 67 */     return arrayOfItemStack;
/*    */   }
/*    */ 
/*    */   public Player loadPlayer(OfflinePlayer paramOfflinePlayer) {
/* 71 */     if ((paramOfflinePlayer == null) || (!paramOfflinePlayer.hasPlayedBefore())) {
/* 72 */       return null;
/*    */     }
/* 74 */     GameProfile localGameProfile = new GameProfile(paramOfflinePlayer.getUniqueId(), paramOfflinePlayer.getName());
/* 75 */     MinecraftServer localMinecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
/* 76 */     EntityPlayer localEntityPlayer = new EntityPlayer(localMinecraftServer, localMinecraftServer.getWorldServer(0), localGameProfile, new PlayerInteractManager(localMinecraftServer.getWorldServer(0)));
/* 77 */     CraftPlayer localCraftPlayer = localEntityPlayer.getBukkitEntity();
/* 78 */     if (localCraftPlayer != null) {
/* 79 */       localCraftPlayer.loadData();
/*    */     }
/*    */ 
/* 82 */     return localCraftPlayer;
/*    */   }
/*    */ }

/* Location:           C:\Users\Dr.JIN\Desktop\MysqlPlayerDataBridge-v2.5.7.jar
 * Qualified Name:     net.craftersland.data.bridge.utils.nms.Version_1_7_R4_Kauldron
 * JD-Core Version:    0.6.0
 */