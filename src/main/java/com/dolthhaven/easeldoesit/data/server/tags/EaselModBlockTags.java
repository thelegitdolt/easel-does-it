package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.data.event.GatherDataEvent;

public class EaselModBlockTags extends BlockTagsProvider {

    public EaselModBlockTags(GatherDataEvent event) {
        super(event.getGenerator(), EaselDoesIt.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(EaselModBlocks.EASEL.get());
//        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(EaselModBlocks.STATUE.get());
    }
}
