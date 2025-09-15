package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EaselModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, EaselDoesIt.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, EaselDoesIt.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> ARTIST_POI = POI_TYPES.register("artist_poi",
            () -> new PoiType(ImmutableSet.copyOf(EaselModBlocks.EASEL.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> ARTIST = VILLAGER_PROFESSIONS.register("artist",
            () -> new VillagerProfession("artist", holder -> holder.value() == ARTIST_POI.get(), holder -> holder.value() == ARTIST_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), EaselModSoundEvents.VILLAGER_WORK_ARTIST.get()));
}
