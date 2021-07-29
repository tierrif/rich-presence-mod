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
        // MinecraftServer server = MinecraftClient.getInstance().getServer();
        if (entry != null) {
            String address = entry.address;
            if (address == null || address.isEmpty()) address = "singleplayer";
            variables.put("address", address);

            String playerCount = entry.playerCountLabel.asString();
            if (playerCount == null || playerCount.isEmpty()) playerCount = "-1";
            variables.put("playerCount", playerCount);
            // variables.put("maxPlayerCount", String.valueOf(server.getMaxPlayerCount()));
        } else {
            variables.put("address", "singleplayer");
            variables.put("playerCount", "-1");
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

    public static String getVariableValue(String name) {
        return variables.get(name);
    }
}
