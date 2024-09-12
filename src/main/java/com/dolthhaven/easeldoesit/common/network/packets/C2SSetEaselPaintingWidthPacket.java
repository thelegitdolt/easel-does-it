package com.dolthhaven.easeldoesit.common.network.packets;

import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSetEaselPaintingWidthPacket {
    private final byte newWidth;

    public C2SSetEaselPaintingWidthPacket(byte newWidth) {
        this.newWidth = newWidth;
    }

    public C2SSetEaselPaintingWidthPacket(FriendlyByteBuf buffer) {
        this(buffer.readByte());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(newWidth);
    }

    public void handle(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof EaselMenu easelMenu) {
                easelMenu.dimensionChangedPre();
                easelMenu.setPaintingWidth(this.newWidth);
                easelMenu.dimensionChangedPost();
            }
        });
    }
}
