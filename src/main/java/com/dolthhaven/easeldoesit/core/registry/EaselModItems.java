package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EaselModItems {
    public static final ItemSubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getItemSubHelper();

    public static final RegistryObject<Item> STATUE = HELPER.createItem("statue", () -> new DoubleHighBlockItem(EaselModBlocks.STATUE.get(), Properties.STATUE));

    public static void setUpTabEditors() {
        CreativeModeTabContentsPopulator.mod(EaselDoesIt.MOD_ID)
                .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .addItemsBefore(Ingredient.of(Blocks.CARTOGRAPHY_TABLE), STATUE);
    }

    public static class Properties {
        public static Item.Properties STATUE = new Item.Properties();
    }
}
