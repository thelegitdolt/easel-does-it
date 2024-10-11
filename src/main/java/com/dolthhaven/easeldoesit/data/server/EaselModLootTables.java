package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.*;

public class EaselModLootTables extends LootTableProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables =
            ImmutableList.of(Pair.of(EaselModBlockLoot::new, LootContextParamSets.BLOCK));

    public EaselModLootTables(GatherDataEvent e) {
        super(e.getGenerator());
    }

    @Override
    public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return tables;
    }

    @Override
    protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationContext context) {
    }

    public static class EaselModBlockLoot extends BlockLoot {
        @Override
        protected void addTables() {
            this.dropSelf(EASEL.get());

//            this.doubleBlock(STATUE);
//            this.doubleBlock(EXPOSED_STATUE);
//            this.doubleBlock(WEATHERED_STATUE);
//            this.doubleBlock(OXIDIZED_STATUE);
//
//            this.doubleBlock(WAXED_STATUE);
//            this.doubleBlock(EXPOSED_WAXED_STATUE);
//            this.doubleBlock(WEATHERED_WAXED_STATUE);
//            this.doubleBlock(OXIDIZED_WAXED_STATUE);
        }

        @Override
        public @NotNull Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getKeys().stream().filter(name -> name.getNamespace()
                    .equals(EaselDoesIt.MOD_ID)).map(ForgeRegistries.BLOCKS::getValue).collect(Collectors.toSet());
        }

        public void doubleBlock(RegistryObject<Block> block) {
            this.add(block.get(), (a) -> this.createSinglePropConditionTable(a, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        }
    }
}
