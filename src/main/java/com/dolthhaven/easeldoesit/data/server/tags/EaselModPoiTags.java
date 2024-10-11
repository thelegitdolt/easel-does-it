package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.data.event.GatherDataEvent;

public class EaselModPoiTags extends PoiTypeTagsProvider {
    public EaselModPoiTags(GatherDataEvent e) {
        super(e.getGenerator(), EaselDoesIt.MOD_ID, e.getExistingFileHelper());
    }

    @Override
    public void addTags() {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).addOptional(
              EaselDoesIt.rl("artist_poi")
        );
    }
}
