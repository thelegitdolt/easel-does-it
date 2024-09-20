package com.dolthhaven.easeldoesit.core.registry;

import net.minecraft.resources.ResourceLocation;

public class EaselModConstants {
    public static ResourceLocation AMBER_DYE = dyeDepot("amber_dye");

    public static ResourceLocation dyeDepot(String path) {
        return new ResourceLocation("dye_depot", path);
    }
}
