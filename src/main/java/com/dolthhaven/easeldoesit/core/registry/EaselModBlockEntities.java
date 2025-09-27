package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.common.block.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.Supplier;

public class EaselModBlockEntities {
    public static final BlockEntitySubRegistryHelper BLOCK_ENTITIES = EaselDoesIt.REGISTRY_HELPER.getBlockEntitySubHelper();

    public static final Supplier<BlockEntityType<EaselBlockEntity>> EASEL_ENTITY = BLOCK_ENTITIES.createBlockEntity("easel", EaselBlockEntity::new, () -> Set.of(BlockEntitySubRegistryHelper.collectBlocks(EaselBlock.class)));
}
