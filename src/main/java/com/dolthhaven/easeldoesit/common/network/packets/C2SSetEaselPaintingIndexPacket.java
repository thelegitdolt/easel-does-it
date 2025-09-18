package com.dolthhaven.easeldoesit.common.network.packets;

import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record C2SSetEaselPaintingIndexPacket(short index) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SSetEaselPaintingIndexPacket> TYPE = new CustomPacketPayload.Type<>(EaselDoesIt.rl("change_easel_painting_index"));
    public static final StreamCodec<ByteBuf, C2SSetEaselPaintingIndexPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT, C2SSetEaselPaintingIndexPacket::index, C2SSetEaselPaintingIndexPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public C2SSetEaselPaintingIndexPacket(int index) {
        this((short) index);
    }

    public static void handleEaselReindexPacket(final C2SSetEaselPaintingIndexPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.containerMenu instanceof EaselMenu easelMenu) {
                    easelMenu.setPaintingIndex(packet.index());
                }
            } else {
                throw new IllegalArgumentException("Hey i'm not sure if this is a thing if it is, FUCK.");
            }
        });
    }
}
