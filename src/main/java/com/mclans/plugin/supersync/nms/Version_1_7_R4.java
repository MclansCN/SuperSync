package com.mclans.plugin.supersync.nms;

import com.mclans.plugin.supersync.ItemStackSerializer;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.Method;






public class Version_1_7_R4
		implements ItemStackSerializer {
	private static Method WRITE_NBT;
	private static Method READ_NBT;


	public String toBase64(org.bukkit.inventory.ItemStack[] paramArrayOfItemStack) {
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
		NBTTagList localNBTTagList = new NBTTagList();

		for (int i = 0; i < paramArrayOfItemStack.length; i++) {
			CraftItemStack localCraftItemStack = getCraftVersion(paramArrayOfItemStack[i]);
			NBTTagCompound localNBTTagCompound = new NBTTagCompound();
			if (localCraftItemStack != null)
				try {
					CraftItemStack.asNMSCopy(localCraftItemStack).save(localNBTTagCompound);
				} catch (NullPointerException localNullPointerException) {
				}
			localNBTTagList.add(localNBTTagCompound);
		}

		if (WRITE_NBT == null) {
			try {
				WRITE_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", NBTBase.class, DataOutput.class);
				WRITE_NBT.setAccessible(true);
			} catch (Exception localException1) {
				throw new IllegalStateException("Unable to find private write method.", localException1);
			}
		}
		try {
			WRITE_NBT.invoke(null, new Object[]{localNBTTagList, localDataOutputStream});
		} catch (Exception localException2) {
			throw new IllegalArgumentException("Unable to write " + localNBTTagList + " to " + localDataOutputStream, localException2);
		}
		return Base64Coder.encodeLines(localByteArrayOutputStream.toByteArray());
	}

	public org.bukkit.inventory.ItemStack[] fromBase64(String paramString) {
		ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(paramString));
		NBTTagList localNBTTagList = (NBTTagList) readNbt(new DataInputStream(localByteArrayInputStream));
		org.bukkit.inventory.ItemStack[] arrayOfItemStack = new org.bukkit.inventory.ItemStack[localNBTTagList.size()];
		for (int i = 0; i < localNBTTagList.size(); i++) {
			NBTTagCompound localNBTTagCompound = localNBTTagList.get(i);
			if (!localNBTTagCompound.isEmpty()) {
				arrayOfItemStack[i] = CraftItemStack.asCraftMirror(net.minecraft.server.v1_7_R4.ItemStack.createStack(localNBTTagCompound));
			}

		}

		return arrayOfItemStack;
	}

	private static NBTBase readNbt(DataInput paramDataInput) {
		if (READ_NBT == null) {
			try {
				READ_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", DataInput.class, Integer.TYPE, NBTReadLimiter.class);
				READ_NBT.setAccessible(true);
			} catch (Exception localException1) {
				throw new IllegalStateException("Unable to find private read method.", localException1);
			}
		}
		try {
			return (NBTBase) READ_NBT.invoke(null, new Object[]{paramDataInput, Integer.valueOf(0), new NBTReadLimiter(9223372036854775807L)});
		} catch (Exception localException2) {
			throw new IllegalArgumentException("Unable to read from " + paramDataInput, localException2);
		}
	}

	private static CraftItemStack getCraftVersion(org.bukkit.inventory.ItemStack paramItemStack) {
		if ((paramItemStack instanceof CraftItemStack))
			return (CraftItemStack) paramItemStack;

		if (paramItemStack != null) {

			CraftItemStack localCraftItemStack = CraftItemStack.asCraftCopy(paramItemStack);

			return localCraftItemStack;
		}

		return null;
	}

	public Player loadPlayer(OfflinePlayer paramOfflinePlayer) {
		if ((paramOfflinePlayer == null) || (!paramOfflinePlayer.hasPlayedBefore())) {
			return null;
		}
		GameProfile localGameProfile = new GameProfile(paramOfflinePlayer.getUniqueId(), paramOfflinePlayer.getName());
		MinecraftServer localMinecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
		EntityPlayer localEntityPlayer = new EntityPlayer(localMinecraftServer, localMinecraftServer.getWorldServer(0), localGameProfile, new PlayerInteractManager(localMinecraftServer.getWorldServer(0)));

		CraftPlayer localCraftPlayer = localEntityPlayer.getBukkitEntity();

		if (localCraftPlayer != null) {

			localCraftPlayer.loadData();
		}
		return localCraftPlayer;
	}
}
