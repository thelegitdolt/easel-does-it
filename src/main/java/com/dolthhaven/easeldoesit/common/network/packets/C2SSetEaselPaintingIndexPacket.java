package com.dolthhaven.easeldoesit.common.network.packets;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record C2SSetEaselPaintingIndexPacket(short index) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SSetEaselPaintingIndexPacket> TYPE = new CustomPacketPayload.Type<>(EaselDoesIt.rl("change_easel_painting_index"));
    public static final StreamCodec<ByteBuf, C2SSetEaselPaintingIndexPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT, C2SSetEaselPaintingIndexPacket::index, C2SSetEaselPaintingIndexPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public C2SSetEaselDimensionsPacket(int index) {
        this((short) index);
    }
}
