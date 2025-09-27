package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class EaselModPaintings {
    public static final ResourceKey<PaintingVariant> CULTURE = create("culture");
    public static final ResourceKey<PaintingVariant> HOLE = create("hole");
    public static final ResourceKey<PaintingVariant> LAYERS = create("layers");
    public static final ResourceKey<PaintingVariant> MONOCHROME = create("monochrome");
    public static final ResourceKey<PaintingVariant> PORTAL = create("portal");
    public static final ResourceKey<PaintingVariant> VINTAGE = create("vintage");

    private static ResourceKey<PaintingVariant> create(String name) {
        return ResourceKey.create(Registries.PAINTING_VARIANT, EaselDoesIt.rl(name));
    }

    private static void register(BootstrapContext<PaintingVariant> context, ResourceKey<PaintingVariant> painting, int width, int height) {
        context.register(painting, new PaintingVariant(width, height, painting.location()));
    }

    public static void bootstrap(BootstrapContext<PaintingVariant> context) {
        register(context, CULTURE, 1, 3);
        register(context, HOLE, 2, 1);
        register(context, LAYERS, 2, 3);
        register(context, MONOCHROME, 2, 4);
        register(context, PORTAL, 3, 2);
        register(context, VINTAGE, 1, 1);
    }
}
