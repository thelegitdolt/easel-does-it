package com.dolthhaven.easeldoesit.core.other;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.ByteBufCodecs;

public class EaselModTrackedData {
    // of the 16 bits of a short:
    // [2][2][12] first 2 is painting width, second 2 is painting length, the 12 is the painting index
    public static final TrackedData<Short> PLAYER_CURRENT_PAINTING_INDEX =
            TrackedData.Builder.create(ByteBufCodecs.SHORT, () -> (short) 0).build();
    public static final TrackedData<Boolean> PAINTING_SHOULD_DROP_SELF =
            TrackedData.Builder.create(ByteBufCodecs.BOOL, () -> false).enablePersistence().enableSaving(Codec.BOOL.fieldOf("value")).build();

    public static void registerTrackedData() {
        TrackedDataManager.INSTANCE.registerData(EaselDoesIt.rl("player_current_painting_index"), PLAYER_CURRENT_PAINTING_INDEX);
        TrackedDataManager.INSTANCE.registerData(EaselDoesIt.rl("painting_should_drop_self"), PAINTING_SHOULD_DROP_SELF);
    }

    public static Vec3i decodePainting(short painting) {
        return new Vec3i(
                (((painting & 0xC000) >>> 14) + 1),
                (((painting & 0x3000) >>> 12) + 1),
                painting & 0x0FFF
        );
    }

    public static short encodePainting(int[] codes) {
        return (short) ((codes[0] << 14) + (codes[1] << 12) + codes[2]);
    }
}
