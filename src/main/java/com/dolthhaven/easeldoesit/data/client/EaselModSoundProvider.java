package com.dolthhaven.easeldoesit.data.client;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.core.registry.EaselModSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class EaselModSoundProvider extends SoundDefinitionsProvider {
    public EaselModSoundProvider(GatherDataEvent e) {
        super(e.getGenerator().getPackOutput(), EaselDoesIt.MOD_ID, e.getExistingFileHelper());
    }

    @Override
    public void registerSounds() {
        this.register("ui/easel/draw1", getUISubtitle(EaselModBlocks.EASEL, "draw_painting"), null);
    }

    private void register(String location, @Nullable String subtitle, Consumer<SoundDefinition.Sound> consumer) {
        SoundDefinition.Sound sound = sound(EaselDoesIt.REGISTRY_HELPER.prefix(location));


        if (consumer != null)
            consumer.accept(sound);

        this.add(EaselModSoundEvents.UI_EASEL_TAKE_RESULT.get(), definition().with(sound).subtitle(subtitle));
    }

    public static String getUISubtitle(RegistryObject<Block> block, String action) {
        return "subtitle.ui." + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).toString().replace(':', '.') + "." + action;
    }
}
