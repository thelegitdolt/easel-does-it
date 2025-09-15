package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.EASEL;

public class EaselModLootTables extends LootTableProvider {
    public EaselModLootTables(GatherDataEvent e) {
        super(e.getGenerator().getPackOutput(), BuiltInLootTables.all(),
                ImmutableList.of(new LootTableProvider.SubProviderEntry(EaselModBlockLoot::new, LootContextParamSets.BLOCK)), e.getLookupProvider());
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {}

    public static class EaselModBlockLoot extends BlockLootSubProvider {
        private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet());

        protected EaselModBlockLoot(HolderLookup.Provider provider) {
            super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        protected void generate() {
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
            return BuiltInRegistries.BLOCK.keySet().stream()
                    .filter(name -> name.getNamespace().equals(EaselDoesIt.MOD_ID))
                    .map(BuiltInRegistries.BLOCK::get).collect(Collectors.toSet());
        }
    }
}
