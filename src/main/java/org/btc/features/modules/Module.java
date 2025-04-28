package org.btc.features.modules;

public interface Module {

    String getName();
    boolean isEnabled();
    void setEnabled(boolean enabled);

    void onEnable();
    void onDisable();

}
