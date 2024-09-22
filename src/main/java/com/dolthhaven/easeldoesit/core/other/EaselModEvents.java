package com.dolthhaven.easeldoesit.core.other;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModPaintings;
import com.dolthhaven.easeldoesit.core.registry.EaselModVillagers;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import com.dolthhaven.easeldoesit.other.util.ModUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.dolthhaven.easeldoesit.common.villagers.EaselModItemListings.*;

@Mod.EventBusSubscriber(modid = EaselDoesIt.MOD_ID)
public class EaselModEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == EaselModVillagers.ARTIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add(new RandomItemsSellingTrade(
                    ModUtil.getAllMembersOfTag(EaselModTags.Items.RARE_DYES),
                    UniformInt.of(3, 3),
                    UniformInt.of(1, 1)));

            trades.get(1).add(new RandomItemsBuyingTrade(
                    ModUtil.getAllDyedItems(str -> new ResourceLocation(str + "_dye")),
                    UniformInt.of(14, 19),
                    UniformInt.of(1, 1)));



            trades.get(2).add(new ItemBuyingTrade(
                    Items.ITEM_FRAME, UniformInt.of(1, 1), UniformInt.of(1, 1), 12,
                    10, 0.01f)
            );

            // villager buy dye
            trades.get(2).add(new RandomItemsBuyingTrade(
                    ModUtil.getAllDyedItems(str -> new ResourceLocation(str + "_dye")),
                    UniformInt.of(1, 1),
                    UniformInt.of(3, 3), 16, 10, 0.01f));

            // villager sell dye
            trades.get(2).add(new RandomItemsSellingTrade(
                    UniformInt.of(1, 1),
                    ModUtil.getAllMembersOfTag(EaselModTags.Items.RARE_DYES),
                    UniformInt.of(3, 3), 16, 10, 0.01f));

            trades.get(2).add(new RandomItemsSellingTrade(
                    UniformInt.of(1, 1),
                    ModUtil.getAllDyedItems(dye -> new ResourceLocation(dye + "_wool")),
                    UniformInt.of(2, 2), 12, 10, 0.01f));



            trades.get(3).add(new RandomItemsBuyingTrade(
                    ModUtil.getAllDyedItems(str -> new ResourceLocation(str + "_dye")),
                    UniformInt.of(14, 19),
                    UniformInt.of(1, 1), 16, 10, 0.01f));

            trades.get(3).add(new RandomItemsSellingTrade(
                    UniformInt.of(1, 1),
                    ModUtil.getAllDyedItems(dye -> new ResourceLocation(dye +  "_terracotta")),
                    UniformInt.of(2, 2), 12, 10, 0.01f
            ));

            trades.get(3).add(new RandomItemsSellingTrade(
                    UniformInt.of(1, 1),
                    ModUtil.getAllDyedItems(dye -> new ResourceLocation(dye +  "_glazed_terracotta")),
                    UniformInt.of(2, 2), 12, 10, 0.01f
            ));



            trades.get(4).add(new RandomItemsSellingTrade(
                    UniformInt.of(1, 1),
                    ModUtil.getAllDyedItems(dye -> new ResourceLocation(dye +  "_glazed_terracotta")),
                    UniformInt.of(2, 2), 12, 10, 0.01f
            ));


            trades.get(5).add(new SellPaintingVariantTrade(
                    EaselModPaintings.CULTURE.get(), UniformInt.of(5, 5), 12
            ));

        }
    }
}
