package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.common.block.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Set;
import java.util.function.Supplier;

@EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EaselModBlockEntities {
    public static final BlockEntitySubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getBlockEntitySubHelper();

    public static final Supplier<BlockEntityType<EaselBlockEntity>> EASEL_ENTITY = HELPER.createBlockEntity("easel", EaselBlockEntity::new, () -> Set.of(BlockEntitySubRegistryHelper.collectBlocks(EaselBlock.class)));
}
