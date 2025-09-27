package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.dolthhaven.easeldoesit.core.registry.EaselModPaintings.*;

public class EaselModPaintingTags extends PaintingVariantTagsProvider {
    public EaselModPaintingTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> provider) {
        super(event.getGenerator().getPackOutput(), provider, EaselDoesIt.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(PaintingVariantTags.PLACEABLE).add(
                PORTAL, LAYERS, HOLE,
                VINTAGE, MONOCHROME
        );

        this.tag(EaselModTags.Paintings.TREASURE).add(CULTURE);
    }
}
