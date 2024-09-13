package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EaselModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, EaselDoesIt.MOD_ID);

    public static final RegistryObject<PaintingVariant> HOLE = register("hole", 32, 16);
    public static final RegistryObject<PaintingVariant> LAYERS = register("layers", 32, 48);
    public static final RegistryObject<PaintingVariant> PORTAL = register("portal", 48, 32);
    public static final RegistryObject<PaintingVariant> VINTAGE = register("vintage", 16, 16);

    private static RegistryObject<PaintingVariant> register(String name, int width, int height) {
        return PAINTING_VARIANTS.register(name, () -> new PaintingVariant(width, height));
    }

}
