package com.dolthhaven.easeldoesit.common.network;

import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselDimensionsPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingIndexPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector2i;

public class ServerEventHandler {
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
