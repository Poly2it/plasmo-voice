package su.plo.lib.api.client.connection;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface MinecraftServerConnection {

    Optional<MinecraftPlayerInfo> getPlayerInfo(@NotNull UUID playerId);
}
