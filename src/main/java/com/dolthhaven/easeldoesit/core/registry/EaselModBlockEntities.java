package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.blocks.EaselBlock;
import com.dolthhaven.easeldoesit.common.blocks.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EaselModBlockEntities {
    public static final BlockEntitySubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getBlockEntitySubHelper();

    public static final RegistryObject<BlockEntityType<EaselBlockEntity>> EASEL = HELPER.createBlockEntity("easel", EaselBlockEntity::new, EaselBlock.class);

}
