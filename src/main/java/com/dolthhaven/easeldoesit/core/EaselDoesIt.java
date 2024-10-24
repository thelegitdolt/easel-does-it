package com.dolthhaven.easeldoesit.core;

import com.dolthhaven.easeldoesit.common.network.EaselModPacketListener;
import com.dolthhaven.easeldoesit.core.other.EaselModCompat;
import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.core.registry.*;
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
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@Mod(EaselDoesIt.MOD_ID)
public class EaselDoesIt
{
    public static final String MOD_ID = "easel_does_it";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    private static final Logger LOGGER = LogUtils.getLogger();

    public EaselDoesIt() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        // do the data set up
        bus.addListener(this::dataSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // register the stuffs!! i love blueprint!!
        REGISTRY_HELPER.register(bus);

        EaselModMenuTypes.MENUS.register(bus);
        EaselModPaintings.PAINTING_VARIANTS.register(bus);
        EaselModVillagers.POI_TYPES.register(bus);
        EaselModVillagers.VILLAGER_PROFESSIONS.register(bus);

        EaselModTrackedData.registerTrackedData();

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
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
            EaselModMenuTypes.registerScreens();
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

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static void log(String str) {
        LOGGER.info(str);
    }
}
