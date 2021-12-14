package su.plo.voice.api.sources;

import su.plo.voice.api.player.VoicePlayer;

import javax.annotation.Nonnull;

public interface DirectAudioSource extends AudioSource {
    /**
     * ru_RU
     * Получить игрока источника
     */
    @Nonnull
    VoicePlayer getPlayer();
}