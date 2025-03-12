package com.dolthhaven.easeldoesit.core;

import com.teamabnormals.blueprint.core.annotations.ConfigKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class EaselModConfig {
    public static class Common {
        @ConfigKey("painting_zombie")
        public final ForgeConfigSpec.BooleanValue paintingZombie;

        @ConfigKey("painting_zombie_chance")
        public final ForgeConfigSpec.DoubleValue paintingSpawnChance;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("painting_zombies");
            paintingZombie = builder.comment("If zombies will have a random chance to spawn a 1x1 painting on their head.")
                            .define("Painting Head", false);
            paintingSpawnChance = builder.comment("If \"Painting Head\" is set to true, chances of a zombie spawning with a painting on their head")
                    .defineInRange("Painting Head Chances", 0.05, 0, 1);

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

    public static boolean canSpawnPainting(RandomSource random) {
        return COMMON.paintingZombie.get() && random.nextDouble() < COMMON.paintingSpawnChance.get();
    }
}
