package com.dolthhaven.easeldoesit.core.other;

import com.dolthhaven.easeldoesit.common.villagers.EaselModItemListings;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModVillagers;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import com.dolthhaven.easeldoesit.other.util.ModUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID)
public class EaselModEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == EaselModVillagers.ARTIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add(new EaselModItemListings.RandomItemsSellingTrade(
                    ModUtil.getAllMembersOfTag(EaselModTags.Items.RARE_DYES),
                    UniformInt.of(1, 1),
                    UniformInt.of(3, 3)));

            trades.get(1).add(new EaselModItemListings.RandomItemsBuyingTrade(
                    Arrays.stream(DyeColor.values()).map(dyecolor -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(dyecolor.getName() + "_dye"))).toList(),
                    UniformInt.of(14, 19),
                    UniformInt.of(1, 1)));
        }
    }
}
