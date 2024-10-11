package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import static com.dolthhaven.easeldoesit.core.other.EaselModConstants.AMBER_DYE;
import static com.dolthhaven.easeldoesit.core.other.EaselModConstants.CORAL_DYE;
import static net.minecraft.world.item.Items.*;

public class EaselModItemTags extends ItemTagsProvider {
    public EaselModItemTags(GatherDataEvent event, BlockTagsProvider blockTags) {
        super(event.getGenerator(), blockTags, EaselDoesIt.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags() {
        this.tag(EaselModTags.Items.RARE_DYES).add(
            GREEN_DYE, BLACK_DYE, BROWN_DYE, CYAN_DYE
        ).addOptional(AMBER_DYE).addOptional(CORAL_DYE);
    }
}
