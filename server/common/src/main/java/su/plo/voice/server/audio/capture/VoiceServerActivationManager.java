package su.plo.voice.server.audio.capture;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import su.plo.voice.api.addon.AddonContainer;
import su.plo.voice.api.addon.AddonManager;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.audio.capture.ServerActivation;
import su.plo.voice.api.server.audio.capture.ServerActivationManager;
import su.plo.voice.api.server.player.PlayerManager;
import su.plo.voice.api.server.player.VoicePlayer;
import su.plo.voice.proto.data.audio.capture.VoiceActivation;
import su.plo.voice.proto.packets.tcp.clientbound.ActivationRegisterPacket;
import su.plo.voice.proto.packets.tcp.clientbound.ActivationUnregisterPacket;
import su.plo.voice.server.config.ServerConfig;
import su.plo.voice.server.player.BaseVoicePlayer;

import java.util.*;

public final class VoiceServerActivationManager implements ServerActivationManager {

    private final PlasmoVoiceServer voiceServer;
    private final AddonManager addons;
    private final PlayerManager players;
    private final Map<UUID, ServerActivation> activationById = Maps.newConcurrentMap();

    public VoiceServerActivationManager(@NotNull PlasmoVoiceServer voiceServer,
                                        @NotNull ServerConfig.Voice voiceConfig) {
        this.voiceServer = voiceServer;
        this.addons = voiceServer.getAddonManager();
        this.players = voiceServer.getPlayerManager();

        Optional<AddonContainer> voiceAddon = addons.getAddon("plasmovoice");
        if (!voiceAddon.isPresent()) throw new IllegalArgumentException("Plasmo Voice addon is not registered");

        register(
                voiceServer,
                VoiceActivation.PROXIMITY_NAME,
                "key.plasmovoice.proximity",
                "plasmovoice:textures/icons/microphone.png",
                voiceConfig.getDistances(),
                voiceConfig.getDefaultDistance(),
                true,
                true,
                1
        );
    }

    @Override
    public Optional<ServerActivation> getActivationById(@NotNull UUID id) {
        return Optional.ofNullable(activationById.get(id));
    }

    @Override
    public Optional<ServerActivation> getActivationByName(@NotNull String name) {
        return Optional.ofNullable(activationById.get(VoiceActivation.generateId(name)));
    }

    @Override
    public Collection<ServerActivation> getActivations() {
        return activationById.values();
    }

    @Override
    public @NotNull ServerActivation register(@NotNull Object addonObject,
                                              @NotNull String name,
                                              @NotNull String translation,
                                              @NotNull String icon,
                                              List<Integer> distances,
                                              int defaultDistance,
                                              boolean transitive,
                                              boolean stereoSupported,
                                              int weight) {
        Optional<AddonContainer> addon = addons.getAddon(addonObject);
        if (!addon.isPresent()) throw new IllegalArgumentException("addonObject is not an addon");

        return activationById.computeIfAbsent(
                VoiceActivation.generateId(name),
                (id) -> {
                    VoiceServerActivation activation = new VoiceServerActivation(
                            voiceServer,
                            addon.get(),
                            name,
                            translation,
                            icon,
                            distances,
                            defaultDistance,
                            transitive,
                            stereoSupported,
                            weight
                    );

                    voiceServer.getTcpConnectionManager()
                            .broadcast(new ActivationRegisterPacket(activation));

                    return activation;
                }
        );
    }

    @Override
    public boolean unregister(@NotNull UUID id) {
        ServerActivation activation = activationById.remove(id);
        if (activation != null) {
            players.getPlayers()
                    .stream()
                    .filter(VoicePlayer::hasVoiceChat)
                    .forEach((player) -> ((BaseVoicePlayer) player).removeActivationDistance(activation));

            voiceServer.getTcpConnectionManager()
                    .broadcast(new ActivationUnregisterPacket(activation.getId()));

            return true;
        }

        return false;
    }

    @Override
    public boolean unregister(@NotNull String name) {
        return unregister(VoiceActivation.generateId(name));
    }

    @Override
    public boolean unregister(@NotNull ServerActivation activation) {
        return unregister(activation.getId());
    }
}