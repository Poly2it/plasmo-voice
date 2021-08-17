package su.plo.voice.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import su.plo.voice.client.VoiceClient;

import java.util.UUID;

public class PlayerVolumeWidget extends SliderWidget {
    private final UUID uuid;

    public PlayerVolumeWidget(UUID uuid) {
        super(0, 0, 0, 20,
                new TranslatableText("gui.plasmo_voice.player_volume", (int) (Math.round(VoiceClient.getClientConfig().getPlayerVolumes().getOrDefault(uuid, 1.0D) * 100.0D)) + "%"),
                VoiceClient.getClientConfig().getPlayerVolumes().getOrDefault(uuid, 1.0D) / 2.0D);
        this.uuid = uuid;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(new TranslatableText("gui.plasmo_voice.player_volume", (int) Math.round(this.value * 200.0D) + "%"));
    }

    @Override
    protected void applyValue() {
        VoiceClient.getClientConfig().getPlayerVolumes().put(uuid, this.value * 2.0D);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int x, int y, int entryWidth, int entryHeight) {
        if (this.visible) {
            this.setWidth((entryWidth / 2) + 2);
            this.x = x + entryHeight;
            this.y = y + 6;

            super.render(matrices, mouseX, mouseY, delta);
        }
    }

    protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHovered() ? 2 : 1) * 20;
        this.drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)), this.y, 0, 46 + i, 4, 20);
        this.drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
    }
}
