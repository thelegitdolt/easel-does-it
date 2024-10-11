package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.dolthhaven.easeldoesit.core.other.EaselModConstants.*;
import static net.minecraft.world.item.Items.*;

public class EaselModItemTags extends ItemTagsProvider {
    public EaselModItemTags(GatherDataEvent event, CompletableFuture<TagLookup<Block>> blockTags) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTags, EaselDoesIt.MOD_ID, event.getExistingFileHelper());
    }


    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        this.tag(EaselModTags.Items.RARE_DYES).add(
            GREEN_DYE, BLACK_DYE, BROWN_DYE, CYAN_DYE
        ).addOptional(AMBER_DYE).addOptional(CORAL_DYE);
    }
}
