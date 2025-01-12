package su.plo.lib.mod.client;

import net.minecraft.client.Minecraft;
import su.plo.lib.api.client.MinecraftOptions;

public final class ModOptions implements MinecraftOptions {

    private final Minecraft minecraft = Minecraft.getInstance();

    @Override
    public boolean isGuiHidden() {
        return minecraft.options.hideGui;
    }

    @Override
    public int getRenderDistance() {
        return minecraft.options.renderDistance().get();
    }

    @Override
    public float getBackgroundOpacity(float m) {
        return minecraft.options.getBackgroundOpacity(m);
    }

    @Override
    public int getBackgroundColor(int m) {
        return minecraft.options.getBackgroundColor(m);
    }
}
