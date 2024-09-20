package com.dolthhaven.easeldoesit.common.villagers;

import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EaselModItemListings {
    public static class RandomItemsBuyingTrade implements VillagerTrades.ItemListing {
        private final List<Item> allowedItems;
        private final UniformInt costCount;
        private final UniformInt emeraldCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public RandomItemsBuyingTrade(List<Item> allowedItems, UniformInt costCount, UniformInt emeraldCount, int maxUses, int xp, float priceMultiplier) {
            this.allowedItems = allowedItems;
            this.costCount = costCount;
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.villagerXp = xp;
            this.priceMultiplier = priceMultiplier;
        }

        public RandomItemsBuyingTrade(List<Item> allowedItems, UniformInt costCount, UniformInt emeraldCount) {
            this(allowedItems, costCount, emeraldCount, 16, 3, 0.05F);
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource source) {
            Item item = allowedItems.get(source.nextInt(allowedItems.size()));
            int cost = costCount.sample(source);
            int emerald = emeraldCount.sample(source);
            return new MerchantOffer(new ItemStack(item, cost), new ItemStack(Items.EMERALD, emerald), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class RandomItemsSellingTrade implements VillagerTrades.ItemListing {
        private final UniformInt emeraldCount;
        private final List<Item> allowedItems;
        private final UniformInt costCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public RandomItemsSellingTrade(UniformInt emeraldCount, List<Item> allowedItems, UniformInt costCount, int maxUses, int xp, float priceMultiplier) {
            this.emeraldCount = emeraldCount;
            this.allowedItems = allowedItems;
            this.costCount = costCount;
            this.maxUses = maxUses;
            this.villagerXp = xp;
            this.priceMultiplier = priceMultiplier;
        }

        public RandomItemsSellingTrade(List<Item> allowedItems, UniformInt costCount, UniformInt emeraldCount) {
            this(emeraldCount, allowedItems, costCount, 16, 3, 0.05F);
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource source) {
            Item item = allowedItems.get(source.nextInt(allowedItems.size()));
            int cost = costCount.sample(source);
            int emerald = emeraldCount.sample(source);
            return new MerchantOffer(new ItemStack(Items.EMERALD, emerald), new ItemStack(item, cost), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
}
