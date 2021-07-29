package io.github.hotlava03.richpresencemod.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import io.github.hotlava03.richpresencemod.RichPresenceListener;
import io.github.hotlava03.richpresencemod.RichPresenceMod;

import java.io.File;

public class Config implements IConfigHandler {
    private static Config instance;
    public static final ImmutableList<IConfigBase> DEFAULT_CONFIG = getDefaultConfig();

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    public static ImmutableList<IConfigBase> getDefaultConfig() {
        return ImmutableList.of(
                new ConfigString("clientId", DefaultValues.CLIENT_ID, "Do not change this unless you know what you're doing. Requires restart to work."),
                new ConfigString("largeImage", DefaultValues.LARGE_IMAGE, "The large image to appear in the rich presence."),
                new ConfigString("smallImage", DefaultValues.SMALL_IMAGE, "The small image to appear in the rich presence."),
                new ConfigString("largeImageText", DefaultValues.LARGE_IMAGE_TEXT, "The text to appear in the large image's label."),
                new ConfigString("smallImageText", DefaultValues.SMALL_IMAGE_TEXT, "The text to appear in the small image's label."),
                new ConfigString("stateIdle", DefaultValues.STATE_IDLE, "The state text when idle (Title Screen)."),
                new ConfigString("stateInServer", DefaultValues.STATE_IN_SERVER, "The state when in a server."),
                new ConfigString("detailsIdle", DefaultValues.DETAILS_IDLE, "The detail text when idle (Title Screen)."),
                new ConfigString("detailsInServer", DefaultValues.DETAILS_IN_SERVER, "The detail text when in a server."),
                new ConfigString("detailsInSingleplayer", DefaultValues.DETAILS_IN_SINGLEPLAYER, "The detail text when in singleplayer")
        );
    }

    public String getStringValue(String key) {
        for (IConfigBase field : DEFAULT_CONFIG) {
            if (key.equalsIgnoreCase(field.getName())) return ((ConfigString) field).getStringValue();
        }
        return null;
    }

    @Override
    public void load() {
        File configFile = new File(FileUtils.getConfigDirectory(), "richpresencemod.json");
        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "RichPresenceMod", DEFAULT_CONFIG);
            }
        }

        RichPresenceListener.reloadRichPresence(RichPresenceMod.getInstance().getClient());
    }

    @Override
    public void save() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();
            ConfigUtils.writeConfigBase(root, "RichPresenceMod", DEFAULT_CONFIG);
            JsonUtils.writeJsonToFile(root, new File(dir, "richpresencemod.json"));
        }
    }
}
