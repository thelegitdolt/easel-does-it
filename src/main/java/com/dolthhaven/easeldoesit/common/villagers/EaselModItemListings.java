package com.dolthhaven.easeldoesit.common.villagers;

import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EaselModItemListings {
    public abstract static class EaselModTrade implements VillagerTrades.ItemListing {
        private final UniformInt costCount;
        private final UniformInt emeraldCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EaselModTrade(UniformInt costCount, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            this.costCount = costCount;
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        public abstract Item getSecondItem(RandomSource random);

        public ItemStack getSecond(RandomSource random) {
                return new ItemStack(getFirstItem(random), emeraldCount.sample(random));
        }

        public abstract Item getFirstItem(RandomSource random);

        public ItemCost getFirst(RandomSource random) {
            return new ItemCost(getSecondItem(random), costCount.sample(random));
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
            return new MerchantOffer(getFirst(random), getSecond(random), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class ItemToEmerald extends EaselModTrade {
        private final Item item;
        public ItemToEmerald(Item item, UniformInt costCount, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            super(costCount, emeraldCount, maxUses, villagerXp, priceMultiplier);
            this.item = item;
        }

        public ItemToEmerald(ResourceLocation loc, UniformInt costCount, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            this(BuiltInRegistries.ITEM.get(loc), costCount, emeraldCount, maxUses, villagerXp, priceMultiplier);
        }

        @Override
        public Item getFirstItem(RandomSource random) {
            return Items.EMERALD;
        }

        @Override
        public Item getSecondItem(RandomSource random) {
            return item;
        }
    }

    public static class EmeraldToItem extends EaselModTrade implements VillagerTrades.ItemListing {
        private final Item item;
        public EmeraldToItem(UniformInt emeraldCount, Item item, UniformInt costCount, int maxUses, int villagerXp, float priceMultiplier) {
            super(costCount, emeraldCount, maxUses, villagerXp, priceMultiplier);
            this.item = item;
        }

        @Override
        public Item getFirstItem(RandomSource random) {
            return Items.EMERALD;
        }

        @Override
        public Item getSecondItem(RandomSource random) {
            return item;
        }
    }


    public static class RandomItemToEmerald extends EaselModTrade implements VillagerTrades.ItemListing {
        private final List<Item> items;
        public RandomItemToEmerald(List<Item> allowedItems, UniformInt costCount, UniformInt emeraldCount, int maxUses, int xp, float priceMultiplier) {
            super(costCount, emeraldCount, maxUses, xp, priceMultiplier);
            this.items = allowedItems;
        }

        public RandomItemToEmerald(List<Item> allowedItems, UniformInt costCount, UniformInt emeraldCount) {
            this(allowedItems, costCount, emeraldCount, 16, 2, 0.05F);
        }

        @Override
        public Item getSecondItem(RandomSource random) {
            return Items.EMERALD;
        }

        @Override
        public Item getFirstItem(RandomSource random) {
            return Util.getRandom(items, random);
        }
    }

    public static class EmeraldToRandomItem extends EaselModTrade implements VillagerTrades.ItemListing {
        private final List<Item> allowedItems;

        public EmeraldToRandomItem(UniformInt emeraldCount, List<Item> allowedItems, UniformInt costCount, int maxUses, int xp, float priceMultiplier) {
            super(costCount, emeraldCount, maxUses, xp, priceMultiplier);
            this.allowedItems = allowedItems;
        }

        public EmeraldToRandomItem(List<Item> allowedItems, UniformInt costCount, UniformInt emeraldCount) {
            this(emeraldCount, allowedItems, costCount, 16, 2, 0.05F);
        }

        @Override
        public Item getSecondItem(RandomSource random) {
            return Util.getRandom(allowedItems, random);
        }

        @Override
        public Item getFirstItem(RandomSource random) {
            return Items.EMERALD;
        }
    }

    public static class EmeraldToPainting extends EmeraldToItem implements VillagerTrades.ItemListing {
        private final PaintingVariant variant;

        public EmeraldToPainting(PaintingVariant variant, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            super(Items.PAINTING, UniformInt.of(1, 1), emeraldCount, maxUses, villagerXp, priceMultiplier);
            this.variant = variant;
        }

        @Override
        public ItemStack getSecond(RandomSource random) {
            return PaintingUtil.makeStack(variant);
        }
    }
}
