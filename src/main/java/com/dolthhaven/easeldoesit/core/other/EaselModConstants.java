package com.dolthhaven.easeldoesit.core.other;

import net.minecraft.resources.ResourceLocation;

public class EaselModConstants {
    public static final String CLAYWORKS = "clayworks";
    public static final String DYE_DEPOT = "dye_depot";
    public static final String CHALK = "chalk";
    public static final String FARMERS_DELIGHT = "farmersdelight";

    public static ResourceLocation AMBER_DYE = dyeDepot("amber_dye");
    public static ResourceLocation CORAL_DYE = dyeDepot("coral_dye");
    public static ResourceLocation CANVAS = farmersDelight("canvas");


    public static ResourceLocation clayworks(String path) {
        return ResourceLocation.fromNamespaceAndPath(CLAYWORKS, path);
    }

    public static ResourceLocation farmersDelight(String path) {
        return ResourceLocation.fromNamespaceAndPath(FARMERS_DELIGHT, path);
    }

    public static ResourceLocation dyeDepot(String path) {
        return ResourceLocation.fromNamespaceAndPath(DYE_DEPOT, path);
    }

    public static ResourceLocation chalk(String path) {
        return ResourceLocation.fromNamespaceAndPath(CHALK, path);
    }
}
