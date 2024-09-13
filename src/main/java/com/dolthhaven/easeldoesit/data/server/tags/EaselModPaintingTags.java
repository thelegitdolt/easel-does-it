package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dolthhaven.easeldoesit.core.registry.EaselModPaintings.*;

import java.util.concurrent.CompletableFuture;

public class EaselModPaintingTags extends PaintingVariantTagsProvider {
    public EaselModPaintingTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, EaselDoesIt.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(PaintingVariantTags.PLACEABLE).add(
                PORTAL.getKey(), LAYERS.getKey(), HOLE.getKey(),
                VINTAGE.getKey()
        );
    }
}
