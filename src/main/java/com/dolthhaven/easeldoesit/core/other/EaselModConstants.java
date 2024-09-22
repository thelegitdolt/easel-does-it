package com.dolthhaven.easeldoesit.core.other;

import net.minecraft.resources.ResourceLocation;

public class EaselModConstants {
    public static final String CLAYWORKS = "clayworks";
    public static final String DYE_DEPOT = "dye_depot";
    public static final String CHALK = "chalk";
    public static final String FARMERS_DELIGHT = "farmersdelight";

    public static ResourceLocation AMBER_DYE = dyeDepot("amber_dye");
    public static ResourceLocation CANVAS = farmersDelight("canvas");


    public static ResourceLocation clayworks(String path) {
        return new ResourceLocation(CLAYWORKS, path);
    }

    public static ResourceLocation farmersDelight(String path) {
        return new ResourceLocation(FARMERS_DELIGHT, path);
    }

    public static ResourceLocation dyeDepot(String path) {
        return new ResourceLocation(DYE_DEPOT, path);
    }

    public static ResourceLocation chalk(String path) {
        return new ResourceLocation(CHALK, path);
    }
}
