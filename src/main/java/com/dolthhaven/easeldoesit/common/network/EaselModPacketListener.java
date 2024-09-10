package com.dolthhaven.easeldoesit.common.network;

import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingHeightPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingIndexPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingWidthPacket;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

public class EaselModPacketListener implements Packet<ServerGamePacketListener> {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;


    @Override
    public void write(@NotNull FriendlyByteBuf byteBuf) {

    }

    private static int id() {
        return packetId++;
    }

    @Override
    public void handle(@NotNull ServerGamePacketListener serverPacket) {

    }

    public static void register() {
        SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(EaselDoesIt.rl("easel_dimensions_changed"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = channel;

        channel.messageBuilder(C2SSetEaselPaintingHeightPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSetEaselPaintingHeightPacket::new)
                .encoder(C2SSetEaselPaintingHeightPacket::encode)
                .consumerMainThread(C2SSetEaselPaintingHeightPacket::handle)
                .add();

        channel.messageBuilder(C2SSetEaselPaintingWidthPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSetEaselPaintingWidthPacket::new)
                .encoder(C2SSetEaselPaintingWidthPacket::encode)
                .consumerMainThread(C2SSetEaselPaintingWidthPacket::handle)
                .add();

        channel.messageBuilder(C2SSetEaselPaintingIndexPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSetEaselPaintingIndexPacket::new)
                .encoder(C2SSetEaselPaintingIndexPacket::encode)
                .consumerMainThread(C2SSetEaselPaintingIndexPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
