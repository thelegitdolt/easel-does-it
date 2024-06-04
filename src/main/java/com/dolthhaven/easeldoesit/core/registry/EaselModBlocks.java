package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.blocks.EaselBlock;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EaselModBlocks {
    public static final BlockSubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<Block> EASEL = HELPER.createFuelBlock("easel", () -> new EaselBlock(WEBlockProps.EASEL), 300);

    public static void setupTabEditors() {
        CreativeModeTabContentsPopulator.mod(EaselDoesIt.MOD_ID)
                .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .addItemsAfter(Ingredient.of(Blocks.LOOM), EASEL);
    }

    public static class WEBlockProps {
        public static final BlockBehaviour.Properties EASEL = BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED).sound(SoundType.WOOD).strength(2.5f).ignitedByLava();
    }



}
