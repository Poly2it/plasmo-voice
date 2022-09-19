package su.plo.voice.lib.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.plo.lib.client.render.VertexBuilder;

@RequiredArgsConstructor
public final class ModVertexBuilder implements VertexBuilder {

    public static @Nullable VertexFormat getFormat(@NotNull Format format) {
        return switch (format) {
            case POSITION -> DefaultVertexFormat.POSITION;
            case POSITION_COLOR -> DefaultVertexFormat.POSITION_COLOR;
            case POSITION_TEX -> DefaultVertexFormat.POSITION_TEX;
            case POSITION_TEX_COLOR -> DefaultVertexFormat.POSITION_TEX_COLOR;
            case POSITION_TEX_SOLID_COLOR -> ModShaders.POSITION_TEX_SOLID_COLOR;
            default -> null;
        };
    }

    private final BufferBuilder builder;

    @Override
    public VertexBuilder begin(@NotNull Mode mode, @NotNull Format format) {
        VertexFormat.Mode mcMode = getMode(mode);
        VertexFormat mcFormat = getFormat(format);
        if (mcMode == null || mcFormat == null) return this;

        builder.begin(mcMode, mcFormat);
        return this;
    }

    @Override
    public VertexBuilder vertex(double x, double y, double z) {
        builder.vertex(x, y, z);
        return this;
    }

    @Override
    public VertexBuilder uv(float u, float v) {
        builder.uv(u, v);
        return this;
    }

    @Override
    public VertexBuilder color(int red, int green, int blue, int alpha) {
        builder.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public VertexBuilder color(float red, float green, float blue, float alpha) {
        builder.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public VertexBuilder endVertex() {
        builder.endVertex();
        return this;
    }

    private @Nullable VertexFormat.Mode getMode(@NotNull Mode mode) {
        return switch (mode) {
            case QUADS -> VertexFormat.Mode.QUADS;
            default -> null;
        };
    }
}