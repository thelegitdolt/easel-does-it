package com.dolthhaven.easeldoesit.core.registry;


import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EaselModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, EaselDoesIt.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, EaselDoesIt.MOD_ID);

    public static final RegistryObject<PoiType> ARTIST_POI = POI_TYPES.register("artist_poi",
            () -> new PoiType(ImmutableSet.copyOf(EaselModBlocks.EASEL.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> ARTIST = VILLAGER_PROFESSIONS.register("artist",
            () -> new VillagerProfession("artist", holder -> holder.get() == ARTIST_POI.get(), holder -> holder.get() == ARTIST_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), EaselModSoundEvents.VILLAGER_WORK_ARTIST.get()));
}
