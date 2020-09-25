package io.github.hotlava03.richpresencemod.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

import java.util.HashMap;
import java.util.Map;

public class VarHandler {
    private static Map<String, String> variables;

    public static void update() {
        variables = new HashMap<>();
        ServerInfo entry = MinecraftClient.getInstance().getCurrentServerEntry();
        if (entry != null) {
            variables.put("address", entry.address);
            variables.put("playerCount", entry.playerCountLabel.asString());
        }
        variables.put("modCount", String.valueOf(FabricLoader.getInstance().getAllMods().size()));
        variables.put("mcVersion", SharedConstants.getGameVersion().getName());
    }

    public static String repl(String original) {
        String toReturn = original;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            toReturn = toReturn.replace("$" + entry.getKey(), entry.getValue());
        }
        return toReturn;
    }
}
