package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagRegistry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EaselModLoot extends LootTableProvider {
    public EaselModLoot(PackOutput output) {
        super(output, BuiltInLootTables.all(), ImmutableList.of(new LootTableProvider.SubProviderEntry(EaselModBlockLoot::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
    }

    public static class EaselModBlockLoot extends BlockLootSubProvider {
        protected EaselModBlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.dropSelf(EaselModBlocks.EASEL.get());
        }

        @Override
        public @NotNull Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block) != null && EaselDoesIt.MOD_ID.equals(ForgeRegistries.BLOCKS.getKey(block).getNamespace())).collect(Collectors.toSet());
        }
    }
}
