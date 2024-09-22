package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;

public class EaselModTags {
    public static class Items {
        public static final TagKey<Item> RARE_DYES = itemTag("rare_dyes");

        private static TagKey<Item> itemTag(String name) {
            return TagUtil.itemTag(EaselDoesIt.MOD_ID, name);
        }
    }

    public static class Paintings {
        public static final TagKey<PaintingVariant> ALWAYS_DROP_ITSELF = paintingTag("always_drop_self");

        private static TagKey<PaintingVariant> paintingTag(String name) {
            return TagKey.create(Registries.PAINTING_VARIANT, EaselDoesIt.rl(name));
        }
    }
}
