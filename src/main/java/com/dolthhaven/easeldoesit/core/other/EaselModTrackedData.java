package com.dolthhaven.easeldoesit.core.other;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;

public class EaselModTrackedData {
    // of the 16 bits of a short:
    // [2][2][12] first 2 is painting width, second 2 is painting length, the 12 is the painting index
    public static final TrackedData<Short> PLAYER_CURRENT_PAINTING_INDEX =
            TrackedData.Builder.create(DataProcessors.SHORT, () -> (short) 0x0).build();
    public static final TrackedData<Boolean> PAINTING_SHOULD_DROP_SELF =
            TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enablePersistence().enableSaving().build();

    public static void registerTrackedData() {
        TrackedDataManager.INSTANCE.registerData(EaselDoesIt.rl("player_current_painting_index"), PLAYER_CURRENT_PAINTING_INDEX);
        TrackedDataManager.INSTANCE.registerData(EaselDoesIt.rl("painting_should_drop_self"), PAINTING_SHOULD_DROP_SELF);
    }

    public static int[] decodePainting(short painting) {
        return new int[]{
                (((painting & 0xC000) >>> 14) + 1) * 16,
                (((painting & 0x3000) >>> 12) + 1) * 16,
                painting & 0x0FFF
        };
    }

    public static short encodePainting(int[] codes) {
        return (short) ((codes[0] << 14) + (codes[1] << 12) + codes[2]);
    }
}
