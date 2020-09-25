package io.github.hotlava03.richpresencemod.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config implements IConfigHandler {
    private static Config instance;
    private final Map<String, IConfigBase> configFields;
    private final ImmutableList<IConfigBase> defaultConfig;

    private Config() {
        this.configFields = new HashMap<>();
        addToMap(new ConfigString("clientId", DefaultValues.CLIENT_ID, "Do not change this unless you know what you're doing. Requires restart to work."));
        addToMap(new ConfigString("largeImage", DefaultValues.LARGE_IMAGE, "The large image to appear in the rich presence."));
        addToMap(new ConfigString("smallImage", DefaultValues.SMALL_IMAGE, "The small image to appear in the rich presence."));
        addToMap(new ConfigString("largeImageText", DefaultValues.LARGE_IMAGE_TEXT, "The text to appear in the large image's label."));
        addToMap(new ConfigString("smallImageText", DefaultValues.SMALL_IMAGE_TEXT, "The text to appear in the small image's label."));
        addToMap(new ConfigString("stateIdle", DefaultValues.STATE_IDLE, "The state text when idle (Title Screen)."));
        addToMap(new ConfigString("stateInServer", DefaultValues.STATE_IN_SERVER, "The state when in a server."));
        addToMap(new ConfigString("detailsIdle", DefaultValues.DETAILS_IDLE, "The detail text when idle (Title Screen)."));
        addToMap(new ConfigString("detailsInServer", DefaultValues.DETAILS_IN_SERVER, "The detail text when in a server."));
        this.defaultConfig = ImmutableList.copyOf(configFields.values());
    }

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    public ImmutableList<IConfigBase> getDefaultConfig() {
        return this.defaultConfig;
    }

    public String getStringValue(String key) {
        return ((ConfigString) configFields.get(key)).getStringValue();
    }

    @Override
    public void load() {
        File configFile = new File(FileUtils.getConfigDirectory(), "richpresencemod.json");

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "RichPresenceMod", getDefaultConfig());
            }
        }
    }

    @Override
    public void save() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();
            ConfigUtils.writeConfigBase(root, "RichPresenceMod", getDefaultConfig());
            JsonUtils.writeJsonToFile(root, new File(dir, "richpresencemod.json"));
        }
    }

    private void addToMap(IConfigBase field) {
        this.configFields.put(field.getName(), field);
    }
}
