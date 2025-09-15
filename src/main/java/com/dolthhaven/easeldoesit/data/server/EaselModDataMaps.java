package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class EaselModDataMaps extends DataMapProvider {
    public EaselModDataMaps(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider());
    }

    @Override
    protected void gather() {
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(EaselModBlocks.EASEL.getId(), new FurnaceFuel(300), false);
    }
}
