package org.btc.features.modules;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void registerModule(Module module) {
        modules.add(module);
    }

    public void enableAll() {
        for (Module module : modules) {
            module.setEnabled(true);
        }
    }

    public void disableAll() {
        for (Module module : modules) {
            module.setEnabled(false);
        }
    }

    public void clear() {
        modules.clear();
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getEnabledModules() {
        List<Module> enabledModules = new ArrayList<>();
        for (Module module : modules) {
            if (module.isEnabled()) {
                enabledModules.add(module);
            }
        }
        return enabledModules;
    }

    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
}
