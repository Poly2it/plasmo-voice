package su.plo.voice.proto.data.source;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import su.plo.voice.proto.data.capture.VoiceActivation;
import su.plo.voice.proto.data.pos.Pos3d;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@NoArgsConstructor
@ToString(callSuper = true)
public final class StaticSourceInfo extends SourceInfo {

    @Getter
    private Pos3d position;
    @Getter
    private Pos3d lookAngle;

    public StaticSourceInfo(@NotNull UUID sourceId,
                            byte state,
                            @NotNull String codec,
                            boolean iconVisible,
                            int angle,
                            Pos3d position,
                            Pos3d lookAngle) {
        super(sourceId, state, codec, VoiceActivation.PROXIMITY_ID, iconVisible, angle);
        this.position = position;
        this.lookAngle = lookAngle;
    }

    @Override
    public void deserialize(ByteArrayDataInput in) {
        super.deserialize(in);

        this.position = new Pos3d();
        position.deserialize(in);
        this.lookAngle = new Pos3d();
        lookAngle.deserialize(in);
    }

    @Override
    public void serialize(ByteArrayDataOutput out) {
        super.serialize(out);

        checkNotNull(position, "position").serialize(out);
        checkNotNull(lookAngle, "lookAngle").serialize(out);
    }

    @Override
    public Type getType() {
        return Type.STATIC;
    }
}