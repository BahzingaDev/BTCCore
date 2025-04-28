package org.btc.features;

import org.btc.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor {

    private final Main plugin;

    public ServerCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){

            if (label.equalsIgnoreCase("btcserver")){

                if (args.length > 0){

                    Player player = (Player) sender;
                    String serverName = args[0];
                    plugin.getServerConnector().connectPlayer(player, serverName);

                }
            }
        }
        return false;
    }
}
