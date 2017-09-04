package com.mclans.plugin.supersync;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface ItemStackSerializer
{
    String toBase64(ItemStack[] paramArrayOfItemStack)
            throws IOException;

    ItemStack[] fromBase64(String paramString)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    Player loadPlayer(OfflinePlayer paramOfflinePlayer);
}
