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
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EaselModItemListings {
    public abstract static class EaselModTrade implements VillagerTrades.ItemListing {
        private final UniformInt firstCount;
        private final UniformInt secondCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EaselModTrade(UniformInt firstCount, UniformInt secondCount, int maxUses, int villagerXp, float priceMultiplier) {
            this.firstCount = firstCount;
            this.secondCount = secondCount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        public abstract Item getFirstItem(RandomSource random, Entity entity);

        public abstract Item getSecondItem(RandomSource random, Entity entity);

        public ItemStack getFirst(RandomSource random, Entity entity) {
            return new ItemStack(getFirstItem(random, entity), firstCount.sample(random));
        }

        public UniformInt getSecondCount() {
            return secondCount;
        }

        public ItemStack getSecond(RandomSource random, Entity entity) {
            return new ItemStack(getSecondItem(random, entity), secondCount.sample(random));
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
            return new MerchantOffer(getFirst(random, entity), getSecond(random, entity), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class ItemToEmerald extends EaselModTrade {
        private final Item item;
        public ItemToEmerald(Item first, UniformInt firstCount, UniformInt secondCount, int maxUses, int villagerXp, float priceMultiplier) {
            super(firstCount, secondCount, maxUses, villagerXp, priceMultiplier);
            this.item = first;
        }

        public ItemToEmerald(ResourceLocation loc, UniformInt costCount, UniformInt emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
            this(BuiltInRegistries.ITEM.get(loc), costCount, emeraldCount, maxUses, villagerXp, priceMultiplier);
        }

        @Override
        public Item getFirstItem(RandomSource random, Entity entity) {
            return item;
        }

        @Override
        public Item getSecondItem(RandomSource random, Entity entity) {
            return Items.EMERALD;
        }
    }

    public static class EmeraldToItem extends EaselModTrade implements VillagerTrades.ItemListing {
        private final Item item;
        public EmeraldToItem(UniformInt firstCount, Item item, UniformInt secondCount, int maxUses, int villagerXp, float priceMultiplier) {
            super(firstCount, secondCount, maxUses, villagerXp, priceMultiplier);
            this.item = item;
        }

        @Override
        public Item getFirstItem(RandomSource random, Entity entity) {
            return Items.EMERALD;
        }

        @Override
        public Item getSecondItem(RandomSource random, Entity entity) {
            return item;
        }
    }


    public static class RandomItemToEmerald extends EaselModTrade implements VillagerTrades.ItemListing {
        private final List<Item> items;
        public RandomItemToEmerald(List<Item> allowedItems, UniformInt firstCount, UniformInt secondCount, int maxUses, int xp, float priceMultiplier) {
            super(firstCount, secondCount, maxUses, xp, priceMultiplier);
            this.items = allowedItems;
        }

        public RandomItemToEmerald(List<Item> allowedItems, UniformInt firstCount, UniformInt secondCount) {
            this(allowedItems, firstCount, secondCount, 16, 2, 0.05F);
        }

        @Override
        public Item getSecondItem(RandomSource random, Entity entity) {
            return Items.EMERALD;
        }

        @Override
        public Item getFirstItem(RandomSource random, Entity entity) {
            return Util.getRandom(items, random);
        }
    }

    public static class EmeraldToRandomItem extends EaselModTrade implements VillagerTrades.ItemListing {
        private final List<Item> allowedItems;

        public EmeraldToRandomItem(UniformInt firstCount, List<Item> allowedItems, UniformInt secondCount, int maxUses, int xp, float priceMultiplier) {
            super(firstCount, secondCount, maxUses, xp, priceMultiplier);
            this.allowedItems = allowedItems;
        }

        public EmeraldToRandomItem(List<Item> allowedItem, UniformInt firstCount, UniformInt secondCount) {
            this(firstCount, allowedItem, secondCount, 16, 2, 0.05F);
        }

        @Override
        public Item getFirstItem(RandomSource random, Entity entity) {
            return Items.EMERALD;
        }

        @Override
        public Item getSecondItem(RandomSource random, Entity entity) {
            return Util.getRandom(allowedItems, random);
        }
    }

    public static class EmeraldToPainting extends EmeraldToItem {
        private final PaintingVariant variant;

        public EmeraldToPainting(PaintingVariant variant, UniformInt firstCount, int maxUses, int villagerXp, float priceMultiplier) {
            super(firstCount, Items.PAINTING, UniformInt.of(1, 1), maxUses, villagerXp, priceMultiplier);
            this.variant = variant;
        }

        @Override
        public ItemStack getSecond(RandomSource random, Entity entity) {
            ItemStack stack = PaintingUtil.makeStack(variant);
            stack.setCount(getSecondCount().sample(random));
            return stack;
        }
    }
}