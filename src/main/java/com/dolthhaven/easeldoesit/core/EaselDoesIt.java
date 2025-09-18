package com.dolthhaven.easeldoesit.core;

import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.core.registry.*;
import com.dolthhaven.easeldoesit.core.registry.other.EaselModRecipeSerializers;
import com.dolthhaven.easeldoesit.data.client.EaselModBlockStates;
import com.dolthhaven.easeldoesit.data.client.EaselModSoundProvider;
import com.dolthhaven.easeldoesit.data.server.EaselModDataMaps;
import com.dolthhaven.easeldoesit.data.server.EaselModDataRegistries;
import com.dolthhaven.easeldoesit.data.server.EaselModLootTables;
import com.dolthhaven.easeldoesit.data.server.EaselModRecipes;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModBlockTags;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModItemTags;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModPaintingTags;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModPoiTags;
import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

/*
4. Fix easel interaction results
5. Put culture in the creative tab in the right place
6. Make treasure painting tooltips blue
7. Make the easel a container
8. Redo my variable names in easelscreen because they are atrocious
9. add clickable pages
 */
@Mod(EaselDoesIt.MOD_ID)
public class EaselDoesIt {
    public static final String MOD_ID = "easel_does_it";
    public static final String GIT_URL = "https://github.com/thelegitdolt/easel-does-it";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    private static final Logger LOGGER = LogUtils.getLogger();

    public EaselDoesIt(IEventBus bus, ModContainer container) {
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);

        EaselModItems.ITEMS.register(bus);
        EaselModBlocks.BLOCKS.register(bus);
        EaselModBlockEntities.BLOCK_ENTITIES.register(bus);
        EaselModSoundEvents.SOUND_EVENTS.register(bus);
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
        EaselModDataRegistries dataRegistries = new EaselModDataRegistries(event);
        dataGen.addProvider(server, dataRegistries);
        CompletableFuture<HolderLookup.Provider> provider = dataRegistries.getRegistryProvider();

        EaselModBlockTags easelModBlockTags = new EaselModBlockTags(event);
        dataGen.addProvider(server, easelModBlockTags);
        dataGen.addProvider(server, new EaselModItemTags(event, easelModBlockTags.contentsGetter()));
        dataGen.addProvider(server, new EaselModPoiTags(event));
        dataGen.addProvider(server, new EaselModPaintingTags(event, provider));
        dataGen.addProvider(server, new EaselModLootTables(event));
        dataGen.addProvider(server, new EaselModRecipes(event));
        dataGen.addProvider(server, new EaselModDataMaps(event));

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
