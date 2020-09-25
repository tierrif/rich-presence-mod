package io.github.hotlava03.richpresencemod;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;

import java.time.OffsetDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class RichPresenceListener implements IPCListener {
    private OffsetDateTime timestamp;
    private Timer timer;

    @Override
    public void onReady(IPCClient client) {
        timestamp = OffsetDateTime.now();
        RichPresence.Builder builder = new RichPresence.Builder()
                .setStartTimestamp(timestamp)
                .setLargeImage("large-logo", "Minecraft")
                .setSmallImage("small-logo", "Rich Presence Mod by HotLava03");
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getCurrentServerEntry() == null) {
            builder.setState("Idle")
                    .setDetails("In title screen");
        } else {
            builder.setState("Playing in " + mc.getCurrentServerEntry().address)
                    .setDetails(mc.getCurrentServerEntry().playerCountLabel.getString() + " players online.");
        }
        client.sendRichPresence(builder.build());
        try {
            client.sendRichPresence(builder.build());
            LogManager.getLogger().info("Rich presence updated.");
        } catch (IllegalStateException e) {
            LogManager.getLogger().warn("Discord is not connected. Skipping rich presence update.");
        }

        this.timer = new Timer();
        startTimer(client);
    }

    public void stopTimer() {
        this.timer.cancel();
    }

    public void startTimer(IPCClient client) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    client.connect(DiscordBuild.ANY);
                } catch (NoDiscordClientException ignore) {
                }
            }
        }, 0, 10000);
    }
}
