package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.common.block.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EaselModBlockEntities {
    public static final BlockEntitySubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getBlockEntitySubHelper();

    public static final RegistryObject<BlockEntityType<EaselBlockEntity>> EASEL_ENTITY = HELPER.createBlockEntity("easel", EaselBlockEntity::new, () -> Set.of(BlockEntitySubRegistryHelper.collectBlocks(EaselBlock.class)));
}
