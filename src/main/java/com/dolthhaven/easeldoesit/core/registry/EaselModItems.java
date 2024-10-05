package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EaselModItems {
    public static final ItemSubRegistryHelper HELPER = EaselDoesIt.REGISTRY_HELPER.getItemSubHelper();

//    public static final RegistryObject<Item> STATUE = HELPER.createItem("statue", () -> new DoubleHighBlockItem(EaselModBlocks.STATUE.get(), Properties.STATUE));
//    public static final RegistryObject<Item> EXPOSED_STATUE = HELPER.createItem("exposed_statue", () -> new DoubleHighBlockItem(EaselModBlocks.EXPOSED_STATUE.get(), Properties.STATUE));
//    public static final RegistryObject<Item> WEATHERED_STATUE = HELPER.createItem("weathered_statue", () -> new DoubleHighBlockItem(EaselModBlocks.WEATHERED_STATUE.get(), Properties.STATUE));
//    public static final RegistryObject<Item> OXIDIZED_STATUE = HELPER.createItem("oxidized_statue", () -> new DoubleHighBlockItem(EaselModBlocks.OXIDIZED_STATUE.get(), Properties.STATUE));
//
//
//    public static final RegistryObject<Item> WAXED_STATUE = HELPER.createItem("waxed_statue", () -> new DoubleHighBlockItem(EaselModBlocks.WAXED_STATUE.get(), Properties.STATUE));
//    public static final RegistryObject<Item> EXPOSED_WAXED_STATUE = HELPER.createItem("exposed_waxed_statue", () -> new DoubleHighBlockItem(EaselModBlocks.EXPOSED_WAXED_STATUE.get(), Properties.STATUE));
//    public static final RegistryObject<Item> WEATHERED_WAXED_STATUE = HELPER.createItem("weathered_waxed_statue", () -> new DoubleHighBlockItem(EaselModBlocks.WEATHERED_WAXED_STATUE.get(), Properties.STATUE));
//    public static final RegistryObject<Item> OXIDIZED_WAXED_STATUE = HELPER.createItem("oxidized_waxed_statue", () -> new DoubleHighBlockItem(EaselModBlocks.OXIDIZED_WAXED_STATUE.get(), Properties.STATUE));



    public static void setUpTabEditors() {
        CreativeModeTabContentsPopulator.mod(EaselDoesIt.MOD_ID)
                .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .addStacksBefore(Ingredient.of(Items.BOOKSHELF), () -> PaintingUtil.createPresetVariantPaintingStack(EaselModPaintings.CULTURE));
    }

    public static class Properties {
        public static Item.Properties STATUE = new Item.Properties();
    }
}
