package com.dolthhaven.easeldoesit.core;

import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModBlockTags;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModLoot;
import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@Mod(EaselDoesIt.MOD_ID)
public class EaselDoesIt {
    public static final String MOD_ID = "easel_does_it";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);


    public EaselDoesIt() {
        ModLoadingContext context = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRY_HELPER.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::dataSetup);

        MinecraftForge.EVENT_BUS.register(this);

//        context.registerConfig(ModConfig.Type.COMMON, EaselModConfig.COMMON_SPEC);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> EaselModBlocks::setupTabEditors);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();

        EaselModBlockTags.TagProvider blockTags = new EaselModBlockTags.TagProvider(output, provider, helper);

        generator.addProvider(server, blockTags);
        generator.addProvider(server, new EaselModLoot(output));
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
