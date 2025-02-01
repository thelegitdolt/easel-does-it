package com.dolthhaven.easeldoesit.core;

import com.teamabnormals.blueprint.core.annotations.ConfigKey;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class EaselModConfig {
    public static class Common {
        @ConfigKey("gallery_abnormals")
        public final ForgeConfigSpec.BooleanValue galleryCompat;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("compat");
            this.galleryCompat = builder.comment("If Gallery's painting selector should be disabled").define("Gallery compat", false);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
