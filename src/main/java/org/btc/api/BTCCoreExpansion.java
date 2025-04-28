package org.btc.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.btc.Main;
import org.btc.features.modules.ModuleManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BTCCoreExpansion extends PlaceholderExpansion {

    private final Main plugin;

    public BTCCoreExpansion(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "btccore";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        ModuleManager manager = plugin.getModuleManager();

        // %btccore_total_modules%
        if (params.equalsIgnoreCase("total_modules")) {
            return String.valueOf(manager.getModules().size());
        }

        // %btccore_enabled_modules%
        if (params.equalsIgnoreCase("enabled_modules")) {
            return String.valueOf(manager.getEnabledModules().size());
        }

        // %btccore_module_status_<name>%
        if (params.startsWith("module_status_")) {
            String moduleName = params.substring("module_status_".length());
            var module = manager.getModule(moduleName);
            if (module == null) return "Unknown";
            return module.isEnabled() ? "Enabled" : "Disabled";
        }

        return null;
    }
}
