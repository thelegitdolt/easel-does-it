package com.dolthhaven.easeldoesit.common.network.packets;

import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.other.util.MathUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
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


    public static byte encode(int width, int height) {
        return (byte) MathUtil.base5From2(width, height);
    }

    public static Vector2i decode(byte b) {
        return new Vector2i(b / 5, b % 5);
    }
    public C2SSetEaselDimensionsPacket(int width, int height) {
        this(encode(width, height));
    }

    public static void handleEaselDimensionPacket(final C2SSetEaselDimensionsPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                Vector2i dims = C2SSetEaselDimensionsPacket.decode(packet.dimensions());
                if (serverPlayer.containerMenu instanceof EaselMenu easelMenu) {
                    easelMenu.setPaintingWidth(dims.x);
                    easelMenu.setPaintingHeight(dims.y);
                }
            } else {
                throw new IllegalArgumentException("Hey i'm not sure if this is a thing if it is, FUCK.");
            }
        });
    }
}
