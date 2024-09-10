package com.dolthhaven.easeldoesit.common.network.packets;

import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSetEaselPaintingIndexPacket {
    private final short newIndex;

    public C2SSetEaselPaintingIndexPacket(short newIndex) {
        this.newIndex = newIndex;
    }

    public C2SSetEaselPaintingIndexPacket(FriendlyByteBuf buffer) {
        this(buffer.readShort());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeShort(newIndex);
    }

    public void handle(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            AbstractContainerMenu menu = player.containerMenu;
            if (menu instanceof EaselMenu easelMenu) {
                easelMenu.setPaintingIndex(this.newIndex);
                easelMenu.indexChanged();
            }
        });
    }
}
