package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModPaintings;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

public class EaselModDataRegistries extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder REGISTRIES = new RegistrySetBuilder()
            .add(Registries.PAINTING_VARIANT, EaselModPaintings::bootstrap);

    public EaselModDataRegistries(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), REGISTRIES, Set.of(EaselDoesIt.MOD_ID));
    }
}
