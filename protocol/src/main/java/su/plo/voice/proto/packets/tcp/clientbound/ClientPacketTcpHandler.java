package su.plo.voice.proto.packets.tcp.clientbound;

import org.jetbrains.annotations.NotNull;
import su.plo.voice.proto.packets.tcp.PacketTcpHandler;

public interface ClientPacketTcpHandler extends PacketTcpHandler {

    void handle(@NotNull ConnectionPacket packet);

    void handle(@NotNull ConfigPlayerInfoPacket packet);

    void handle(@NotNull ConfigPacket packet);
}