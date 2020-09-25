package io.github.hotlava03.richpresencemod.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import io.github.hotlava03.richpresencemod.config.Config;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class ConfigGui extends GuiConfigsBase {
    public ConfigGui() {
        super(10, 50, "rich-presence-mod", null, "Rich Presence Mod config");
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(Config.getInstance().getDefaultConfig());
    }

    @Override
    protected void onSettingsChanged() {
        super.onSettingsChanged();
        Config.getInstance().save();
        Config.getInstance().load();
        LogManager.getLogger().info("Saved settings.");
    }
}