package com.mclans.plugin.supersync.bungeecord.handler;

import com.mclans.plugin.supersync.bungeecord.SuperSyncBC;
import com.mclans.plugin.supersync.entity.PackData;
import com.mclans.plugin.supersync.entity.PlayerData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnector;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerConnectHandler implements Listener {
    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ////////////try to create fake player by bungeecord plugin
        // to keep a communication connection with spigot/////////////////
        ///////////////but not success yet////////////////
        ServerInfo target = event.getTarget();
        ChannelInitializer initializer = new ChannelInitializer(target)
        {
            @Override
            protected void initChannel(Channel ch)
                    throws Exception
            {
                PipelineUtils.BASE.initChannel(ch);
                ListenerInfo listener = ch.attr(PipelineUtils.LISTENER).get();
                InitialHandler init = new InitialHandler(BungeeCord.getInstance(),listener);
                //////////////dont known how to set the argument "ChannelWarp()"//////////////////////
                UserConnection userconnection = new UserConnection(BungeeCord.getInstance(), new ChannelWrapper(), event.getTarget().getName() + "connector", init);
                ch.pipeline().addAfter("frame-decoder", "packet-decoder", new MinecraftDecoder(Protocol.HANDSHAKE, false, event.getPlayer().getPendingConnection().getVersion()));
                ch.pipeline().addAfter("frame-prepender", "packet-encoder", new MinecraftEncoder(Protocol.HANDSHAKE, false, event.getPlayer().getPendingConnection().getVersion()));
                ch.pipeline().get(HandlerBoss.class).setHandler(new ServerConnector(BungeeCord.getInstance(), userconnection, new BungeeServerInfo(target.getName(), target.getAddress(), target.getMotd(), false)));
            }
        };
        //////////////////////////////////////////////////////
        if(player.getServer() == null) return;
        SuperSyncBC.log.info(String.valueOf(SuperSyncBC.playerdatamap.get(player.getUniqueId()).isUpdated()));
        if(SuperSyncBC.playerdatamap.get(player.getUniqueId()).isUpdated()) return;

        event.setCancelled(true);
        PlayerData playerdata = SuperSyncBC.playerdatamap.get(event.getPlayer().getUniqueId());
        playerdata.setTargetServerName(event.getTarget().getName());

        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        ObjectOutputStream obj = null;
        try {
            obj = new ObjectOutputStream(bytes);
            PackData pack = new PackData("SavePlayerData", null);
            obj.writeObject(pack);
            player.getServer().sendData("SuperSync", bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(new TextComponent("正在传送..."));
    }
}
