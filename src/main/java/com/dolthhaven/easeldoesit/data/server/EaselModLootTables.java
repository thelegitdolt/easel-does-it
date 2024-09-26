package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.PackOutput;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.EASEL;
import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.STATUE;

public class EaselModLootTables extends LootTableProvider {
    public EaselModLootTables(PackOutput packOutput) {
        super(packOutput, BuiltInLootTables.all(), ImmutableList.of(
                new LootTableProvider.SubProviderEntry(EaselModBlockLoot::new, LootContextParamSets.BLOCK)
        ));
    }

    @Override
    protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationContext context) {
    }

    public static class EaselModBlockLoot extends BlockLootSubProvider {
        private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet());

        protected EaselModBlockLoot() {
            super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.dropSelf(EASEL.get());
            this.doubleBlock(STATUE);
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
