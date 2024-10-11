package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraftforge.data.event.GatherDataEvent;

import static com.dolthhaven.easeldoesit.core.registry.EaselModPaintings.*;

public class EaselModPaintingTags extends PaintingVariantTagsProvider {
    public EaselModPaintingTags(GatherDataEvent event) {
        super(event.getGenerator(), EaselDoesIt.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    public void addTags() {
        this.tag(PaintingVariantTags.PLACEABLE).add(
                PORTAL.getKey(), LAYERS.getKey(), HOLE.getKey(),
                VINTAGE.getKey(), MONOCHROME.getKey(), CULTURE.getKey()
        );

        this.tag(EaselModTags.Paintings.TREASURE).add(
                CULTURE.getKey()
        );
    }
}
