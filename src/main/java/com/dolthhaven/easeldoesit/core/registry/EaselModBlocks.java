package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

public class EaselModBlocks {
    public static final BlockSubRegistryHelper BLOCKS = EaselDoesIt.REGISTRY_HELPER.getBlockSubHelper();

    public static final DeferredBlock<Block> EASEL = BLOCKS.createBlock("easel", () -> new EaselBlock(Properties.EASEL));


//    public static final RegistryObject<Block> STATUE = HELPER.createBlockNoItem("statue", () -> new WeatheringVillagerStatueBlock(Properties.STATUE));
//    public static final RegistryObject<Block> EXPOSED_STATUE = HELPER.createBlockNoItem("exposed_statue", () -> new WeatheringVillagerStatueBlock(Properties.STATUE));
//    public static final RegistryObject<Block> WEATHERED_STATUE = HELPER.createBlockNoItem("weathered_statue", () -> new WeatheringVillagerStatueBlock(Properties.STATUE));
//    public static final RegistryObject<Block> OXIDIZED_STATUE = HELPER.createBlockNoItem("oxidized_statue", () -> new WeatheringVillagerStatueBlock(Properties.STATUE));
//
//
//    public static final RegistryObject<Block> WAXED_STATUE = HELPER.createBlockNoItem("waxed_statue", () -> new VillagerStatueBlock(Properties.STATUE));
//    public static final RegistryObject<Block> EXPOSED_WAXED_STATUE = HELPER.createBlockNoItem("exposed_waxed_statue", () -> new VillagerStatueBlock(Properties.STATUE));
//    public static final RegistryObject<Block> WEATHERED_WAXED_STATUE = HELPER.createBlockNoItem("weathered_waxed_statue", () -> new VillagerStatueBlock(Properties.STATUE));
//    public static final RegistryObject<Block> OXIDIZED_WAXED_STATUE = HELPER.createBlockNoItem("oxidized_waxed_statue", () -> new VillagerStatueBlock(Properties.STATUE));


    public static void setUpTabEditors() {
        CreativeModeTabContentsPopulator.mod(EaselDoesIt.MOD_ID)
                .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .addItemsBefore(Ingredient.of(Blocks.CARTOGRAPHY_TABLE), EASEL);
    }

    public static class Properties {
        public static final BlockBehaviour.Properties EASEL = BlockBehaviour.Properties.ofFullCopy(Blocks.LECTERN).mapColor(MapColor.COLOR_RED);
        public static final BlockBehaviour.Properties STATUE = BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK);
    }
}
