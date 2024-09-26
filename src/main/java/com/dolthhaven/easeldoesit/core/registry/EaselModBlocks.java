package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.common.block.VillagerStatueBlock;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EaselModBlocks {
    public static final BlockSubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<Block> EASEL = HELPER.createFuelBlock("easel", () -> new EaselBlock(Properties.EASEL), 300);
    public static final RegistryObject<Block> STATUE = HELPER.createBlockNoItem("statue", () -> new VillagerStatueBlock(Properties.STATUE));

    public static void setUpTabEditors() {
        CreativeModeTabContentsPopulator.mod(EaselDoesIt.MOD_ID)
                .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .addItemsBefore(Ingredient.of(Blocks.CARTOGRAPHY_TABLE), EASEL);
    }

    public static class Properties {
        public static final BlockBehaviour.Properties EASEL = BlockBehaviour.Properties.copy(Blocks.LECTERN).mapColor(MapColor.COLOR_RED);
        public static final BlockBehaviour.Properties STATUE = BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK);
    }
}
