package org.btc.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerConnector {

    private final JavaPlugin plugin;

    public ServerConnector(JavaPlugin plugin) {
        this.plugin = plugin;

        // Register the outgoing plugin messaging channel
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    public void connectPlayer(Player player, String serverName) {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteArray);

            out.writeUTF("Connect");
            out.writeUTF(serverName);

            player.sendPluginMessage(plugin, "BungeeCord", byteArray.toByteArray());

        } catch (IOException e) {
            plugin.getLogger().warning("Failed to send player to server " + serverName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void shutdown() {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin);
    }
}
