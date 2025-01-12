package su.plo.voice.socket;

import com.google.common.io.ByteStreams;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import su.plo.voice.proto.packets.udp.PacketUdp;
import su.plo.voice.proto.packets.udp.PacketUdpCodec;

import java.util.List;
import java.util.Optional;

public final class NettyPacketUdpDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
        int readableBytes = packet.content().readableBytes();
        if (readableBytes <= 0) return;

        byte[] bytes = new byte[readableBytes];
        packet.content().readBytes(bytes);

        Optional<PacketUdp> packetUdp = PacketUdpCodec.decode(ByteStreams.newDataInput(bytes));
        if (!packetUdp.isPresent()) return;
        PacketUdp decoded = packetUdp.get();

        if (System.currentTimeMillis() - decoded.getTimestamp() > PacketUdp.TTL) return;

        out.add(new NettyPacketUdp(decoded, packet.sender()));
    }
}
