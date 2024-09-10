package com.dolthhaven.easeldoesit.common.network.packets;

import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSetEaselPaintingHeightPacket {
    private final byte newHeight;


    public C2SSetEaselPaintingHeightPacket(byte newHeight) {
        this.newHeight = newHeight;
    }

    public C2SSetEaselPaintingHeightPacket(FriendlyByteBuf buffer) {
        this(buffer.readByte());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(newHeight);
    }

    public void handle(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof EaselMenu easelMenu) {
                easelMenu.setPaintingHeight(this.newHeight);
                easelMenu.dimensionChanged();
            }
        });
    }
}
