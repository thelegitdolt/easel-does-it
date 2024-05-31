//package com.dolthhaven.easeldoesit.core;
//
//import net.minecraftforge.common.ForgeConfigSpec;
//import org.apache.commons.lang3.tuple.Pair;
//
//public class EaselModConfig {
//    public static class Common {
//        Common(ForgeConfigSpec.Builder builder) {
//        }
//    }
//
//    public static final ForgeConfigSpec COMMON_SPEC;
//    public static final Common COMMON;
//
//
//    static {
//        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
//        COMMON_SPEC = commonSpecPair.getRight();
//        COMMON = commonSpecPair.getLeft();
//    }
//}
