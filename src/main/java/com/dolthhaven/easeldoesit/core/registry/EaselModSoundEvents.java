package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class EaselModSoundEvents {
    public static final SoundSubRegistryHelper SOUND_EVENTS = EaselDoesIt.REGISTRY_HELPER.getSoundSubHelper();

    public static final DeferredHolder<SoundEvent, SoundEvent> UI_EASEL_TAKE_RESULT = SOUND_EVENTS.createSoundEvent("ui.easel.take_result");
    public static final DeferredHolder<SoundEvent, SoundEvent> VILLAGER_WORK_ARTIST = SOUND_EVENTS.createSoundEvent("entity.villager.work_artist");

}
