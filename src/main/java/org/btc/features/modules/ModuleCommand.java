package org.btc.features.modules;

import org.btc.Main;
import org.btc.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ModuleCommand implements CommandExecutor {

    private final Main plugin;
    private final ModuleManager moduleManager;

    public ModuleCommand(Main plugin, ModuleManager moduleManager) {
        this.plugin = plugin;
        this.moduleManager = moduleManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("btccore.modules.manage")) {
            sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &cYou don’t have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &eUsage: /modules <list|reload|enable|disable>"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "list":
                sender.sendMessage(TextUtil.color("&8------ &6Loaded Modules &8------"));
                moduleManager.getModules().forEach(module -> {
                    String name = module.getName();
                    String status = module.isEnabled() ? "&aENABLED" : "&cDISABLED";
                    sender.sendMessage(TextUtil.color("&8• &f" + name + " &7→ " + status));
                });
                return true;

            case "reload":
                if (args.length > 1) {
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &cYou can't reload individual modules yet."));
                    return true;
                }

                sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &eReloading all modules..."));
                moduleManager.disableAll();
                moduleManager.clear();
                ModuleRegistrar.registerAll(plugin, moduleManager);
                moduleManager.enableAll();
                sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &aModules reloaded."));
                return true;


            case "enable":
                if (args.length < 2) {
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &cUsage: /modules enable <module>"));
                    return true;
                }
                String enableName = args[1];
                Module enableModule = moduleManager.getModule(enableName);
                if (enableModule == null) {
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &cModule not found: " + enableName));
                } else if (enableModule.isEnabled()) {
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &eModule already enabled."));
                } else {
                    enableModule.setEnabled(true);
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &aModule '" + enableName + "' enabled."));
                }
                return true;

            case "disable":
                if (args.length < 2) {
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &cUsage: /modules disable <module>"));
                    return true;
                }
                String disableName = args[1];
                Module disableModule = moduleManager.getModule(disableName);
                if (disableModule == null) {
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &cModule not found: " + disableName));
                } else if (!disableModule.isEnabled()) {
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &eModule already disabled."));
                } else {
                    disableModule.setEnabled(false);
                    sender.sendMessage(TextUtil.color("&6&lBTC&f&lMC &8&m|&r &aModule '" + disableName + "' disabled."));
                }
                return true;

            default:
                sender.sendMessage(TextUtil.color("&cUnknown subcommand. Try &e/modules list"));
                return true;
        }
    }
}
