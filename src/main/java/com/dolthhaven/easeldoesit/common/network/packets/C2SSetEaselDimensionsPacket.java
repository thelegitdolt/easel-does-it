package com.dolthhaven.easeldoesit.common.network.packets;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.joml.Vector2i;

public record C2SSetEaselDimensionsPacket(byte dimensions) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SSetEaselDimensionsPacket> TYPE =
            new CustomPacketPayload.Type<>(EaselDoesIt.rl("c2s_easel_dimensions_change"));
    public static final StreamCodec<ByteBuf, C2SSetEaselDimensionsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, C2SSetEaselDimensionsPacket::dimensions, C2SSetEaselDimensionsPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static int a(int a) {
        return a == 48 ? 8 : (a == 8 ? 48 : a);
    }

    public static byte encode(int width, int height) {
        width = a(width) << 1;
        height = a(height) >> 3;
        return (byte) (width + height);
    }

    public static Vector2i decode(byte b) {
        int x = (b & 0xf0) >> 1;
        int y = (b & 0x0f) << 3;
        return new Vector2i(a(x), a(y));
    }

    public C2SSetEaselDimensionsPacket(int width, int height) {
        this(encode(width, height));
    }
}
