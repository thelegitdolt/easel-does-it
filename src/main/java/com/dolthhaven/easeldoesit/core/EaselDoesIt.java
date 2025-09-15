package com.dolthhaven.easeldoesit.core;

import com.dolthhaven.easeldoesit.core.other.EaselModCompat;
import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.core.registry.*;
import com.dolthhaven.easeldoesit.core.registry.other.EaselModRecipeSerializers;
import com.dolthhaven.easeldoesit.data.client.EaselModBlockStates;
import com.dolthhaven.easeldoesit.data.client.EaselModSoundProvider;
import com.dolthhaven.easeldoesit.data.server.EaselModLootTables;
import com.dolthhaven.easeldoesit.data.server.EaselModRecipes;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModBlockTags;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModItemTags;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModPaintingTags;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModPoiTags;
import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

/*
1. REGISTER THE PAINTINGS.
2. Fix the registies.
3. Make easels smelt 300 ticks.
 */
@Mod(EaselDoesIt.MOD_ID)
public class EaselDoesIt {
    public static final String MOD_ID = "easel_does_it";
    public static final String GIT_URL = "https://github.com/thelegitdolt/easel-does-it";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    private static final Logger LOGGER = LogUtils.getLogger();

    public EaselDoesIt(IEventBus bus, ModContainer container) {
        ModLoadingContext context = ModLoadingContext.get();

        // Register the commonSetup method for modloading
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        // do the data set up
        bus.addListener(this::dataSetup);


        REGISTRY_HELPER.register(bus);

        EaselModMenuTypes.MENUS.register(bus);
        EaselModVillagers.POI_TYPES.register(bus);
        EaselModRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        EaselModVillagers.VILLAGER_PROFESSIONS.register(bus);

        EaselModTrackedData.registerTrackedData();
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EaselModCompat.doCompat();
            EaselModPacketListener.register();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EaselModBlocks.setUpTabEditors();
            EaselModItems.setUpTabEditors();
        });
    }

    private void dataSetup(final GatherDataEvent event) {
        DataGenerator dataGen = event.getGenerator();

        boolean server = event.includeServer();
        EaselModBlockTags easelModBlockTags = new EaselModBlockTags(event);
        dataGen.addProvider(server, easelModBlockTags);
        dataGen.addProvider(server, new EaselModItemTags(event, easelModBlockTags.contentsGetter()));
        dataGen.addProvider(server, new EaselModPoiTags(event));
        dataGen.addProvider(server, new EaselModPaintingTags(event));
        dataGen.addProvider(server, new EaselModLootTables(event));
        dataGen.addProvider(server, new EaselModRecipes(event));

        boolean client = event.includeClient();
        dataGen.addProvider(client, new EaselModBlockStates(event));
        dataGen.addProvider(client, new EaselModSoundProvider(event));
    }

    public static void log(String str) {
        LOGGER.info(str);
    }

    public static void warnLog(String str) {
        LOGGER.warn(str);
    }
}
