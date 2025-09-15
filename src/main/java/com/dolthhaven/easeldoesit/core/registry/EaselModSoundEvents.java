package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EaselModSoundEvents {
    private static final SoundSubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getSoundSubHelper();

    public static final DeferredHolder<SoundEvent, SoundEvent> UI_EASEL_TAKE_RESULT = HELPER.createSoundEvent("ui.easel.take_result");
    public static final DeferredHolder<SoundEvent, SoundEvent> VILLAGER_WORK_ARTIST = HELPER.createSoundEvent("entity.villager.work_artist");

}
