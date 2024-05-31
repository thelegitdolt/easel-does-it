package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.teamabnormals.blueprint.core.data.server.tags.BlueprintBlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EaselModBlockTags {
    public static class TagProvider extends BlueprintBlockTagsProvider {

        public TagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
            super(EaselDoesIt.MOD_ID, output, provider, helper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            this.tag(BlockTags.MINEABLE_WITH_AXE)
                    .add(EaselModBlocks.EASEL.get());
        }
    }
}
