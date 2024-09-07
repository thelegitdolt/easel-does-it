package com.dolthhaven.easeldoesit.core;

import com.dolthhaven.easeldoesit.data.client.EaselModBlockStates;
import com.dolthhaven.easeldoesit.data.server.EaselModLootTables;
import com.dolthhaven.easeldoesit.data.server.EaselModRecipes;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModBlockTags;
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

        // do the data set up
        bus.addListener(this::dataSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        // register the items i love blueprint!!
        REGISTRY_HELPER.register(bus);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void dataSetup(final GatherDataEvent event) {
        DataGenerator dataGen = event.getGenerator();
        PackOutput packOutput = dataGen.getPackOutput();
        CompletableFuture<Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();
        EaselModBlockTags easelModBlockTags = new EaselModBlockTags(packOutput, provider, helper);
        dataGen.addProvider(server, easelModBlockTags);
        dataGen.addProvider(server, new EaselModLootTables(packOutput));
        dataGen.addProvider(server, new EaselModRecipes(packOutput));

        boolean client = event.includeClient();
        dataGen.addProvider(client, new EaselModBlockStates(packOutput, helper));
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
}
