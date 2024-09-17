package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EaselModSoundEvents {
    private static final SoundSubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getSoundSubHelper();

    public static final RegistryObject<SoundEvent> UI_EASEL_TAKE_RESULT = HELPER.createSoundEvent("ui.easel.take_result");
}
