package com.dolthhaven.easeldoesit.data.server.tags;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class EaselModTags {
    public static class Items {
        public static final TagKey<Item> RARE_DYES = itemTag("rare_dyes");

        private static TagKey<Item> itemTag(String name) {
            return TagUtil.itemTag(EaselDoesIt.MOD_ID, name);
        }
    }

}
