package com.dolthhaven.easeldoesit.common.villagers;

import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EaselModItemListings {
    public static class ItemBuyingTrade implements VillagerTrades.ItemListing {
        private final Item boughtItem;
        private final UniformInt costCount;
        private final UniformInt emeraldCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemBuyingTrade(Item boughtItem, UniformInt costCount, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            this.boughtItem = boughtItem;
            this.costCount = costCount;
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        public ItemBuyingTrade(ResourceLocation itemRl, UniformInt costCount, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            this(ForgeRegistries.ITEMS.getValue(itemRl), costCount, emeraldCount, maxUses, villagerXp, priceMultiplier);
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
            int cost = costCount.sample(random);
            int emerald = emeraldCount.sample(random);
            return new MerchantOffer(new ItemStack(boughtItem, cost), new ItemStack(Items.EMERALD, emerald), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }


    public static class ItemSellingTrade implements VillagerTrades.ItemListing {
        private final Item boughtItem;
        private final UniformInt costCount;
        private final UniformInt emeraldCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemSellingTrade(Item boughtItem, UniformInt costCount, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            this.boughtItem = boughtItem;
            this.costCount = costCount;
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
            int cost = costCount.sample(random);
            int emerald = emeraldCount.sample(random);
            return new MerchantOffer(new ItemStack(boughtItem, cost), new ItemStack(Items.EMERALD, emerald), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }


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
            this(allowedItems, costCount, emeraldCount, 16, 2, 0.05F);
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
            this(emeraldCount, allowedItems, costCount, 16, 2, 0.05F);
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

    public static class SellPaintingVariantTrade implements VillagerTrades.ItemListing {
        private final PaintingVariant variant;
        private final UniformInt emeraldCost;
        private final int maxUses;

        public SellPaintingVariantTrade(PaintingVariant variant, UniformInt emeraldCost, int maxUses) {
            this.variant = variant;
            this.emeraldCost = emeraldCost;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@NotNull Entity p_219693_, @NotNull RandomSource p_219694_) {
            ItemStack paintingStack = PaintingUtil.getStackFromPainting(this.variant);
            ItemStack emeraldStack = new ItemStack(Items.EMERALD, emeraldCost.sample(p_219694_));

            return new MerchantOffer(emeraldStack, paintingStack, maxUses, 25, 0.01F);
        }
    }


    public static class EitherOrTrade implements VillagerTrades.ItemListing {
        private final VillagerTrades.ItemListing tradeOne;
        private final VillagerTrades.ItemListing tradeTwo;

        public EitherOrTrade(VillagerTrades.ItemListing tradeOne, VillagerTrades.ItemListing tradeTwo) {
            this.tradeOne = tradeOne;
            this.tradeTwo = tradeTwo;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
            return random.nextBoolean() ? tradeOne.getOffer(entity, random) : tradeTwo.getOffer(entity, random);
        }
    }
}
