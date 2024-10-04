package com.dolthhaven.easeldoesit.common.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.function.Supplier;

public interface EaselModWeatheringCopper extends WeatheringCopper {
//    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> HashBiMap.create(ImmutableBiMap.<Block, Block>builder()
//            .put()
//            .build()));
}
