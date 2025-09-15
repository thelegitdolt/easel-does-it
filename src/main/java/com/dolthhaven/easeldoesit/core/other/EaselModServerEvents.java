package com.dolthhaven.easeldoesit.core.other;

import com.dolthhaven.easeldoesit.common.network.ServerEventHandler;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselDimensionsPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingIndexPacket;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModPaintings;
import com.dolthhaven.easeldoesit.core.registry.EaselModVillagers;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import com.dolthhaven.easeldoesit.other.util.ModUtil;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dolthhaven.easeldoesit.common.villagers.EaselModItemListings.*;

@EventBusSubscriber(modid = EaselDoesIt.MOD_ID)
public class EaselModServerEvents {
    private static final UniformInt ONE = UniformInt.of(1, 1);

    private static UniformInt constant(int i) {
        return UniformInt.of(i, i);
    }

    @SubscribeEvent
    public static void changeCreativeTab(BuildCreativeModeTabContentsEvent event) {
        Set<ItemStack> shouldRemoveFromCreativeTab = event.getParameters().holders().asGetterLookup()
                .lookupOrThrow(Registries.PAINTING_VARIANT).getOrThrow(EaselModTags.Paintings.TREASURE).stream()
                .map(holder -> PaintingUtil.makeStack(holder.value())).collect(Collectors.toSet());

        for (ItemStack stack : shouldRemoveFromCreativeTab) {
            event.remove(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar reg = event.registrar("1");
        reg.playToServer(C2SSetEaselDimensionsPacket.TYPE, C2SSetEaselDimensionsPacket.STREAM_CODEC, ServerEventHandler::handleEaselDimensionPacket);
        reg.playToServer(C2SSetEaselPaintingIndexPacket.TYPE, C2SSetEaselPaintingIndexPacket.CODEC, ServerEventHandler::handleEaselReindexPacket);
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == EaselModVillagers.ARTIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            List<Item> dyes = ModUtil.getAllDyedItems(str -> ResourceLocation.withDefaultNamespace(str + "_dye"));
            List<Item> rare_dyes = ModUtil.getAllMembersOfTag(EaselModTags.Items.RARE_DYES);


            trades.get(1).add(new EmeraldToRandomItem(
                    rare_dyes, constant(3),
                                ONE));

            trades.get(1).add(new RandomItemToEmerald(
                    dyes, UniformInt.of(14, 19),
                            ONE));


            trades.get(2).add(new EmeraldToItem(
                                    ONE,
                    Items.ITEM_FRAME, ONE, 12,
                    10, 0.01f)
            );


            // villager buy dye
            trades.get(2).add(new RandomItemToEmerald(
                    dyes, UniformInt.of(14, 19),
                        ONE,16, 10, 0.01f));
            // villager sell dye
            trades.get(2).add(new EmeraldToRandomItem(
                                ONE,
                    rare_dyes, UniformInt.of(3, 3), 16, 10, 0.01f));
            trades.get(2).add(new EmeraldToRandomItem(
                                                                                                                        ONE,
                    ModUtil.getAllDyedItems(dye -> ResourceLocation.withDefaultNamespace(dye + "_wool")), UniformInt.of(2, 2),
                    12, 10, 0.01f));
            trades.get(2).add(new ItemToEmerald(
                    Items.INK_SAC, UniformInt.of(3, 5),
                                    ONE, 16, 10, 0.01f));
            if (ModList.get().isLoaded(EaselModConstants.FARMERS_DELIGHT))
                trades.get(2).add(new ItemToEmerald(
                    EaselModConstants.CANVAS, UniformInt.of(6, 8),
                                                ONE, 16, 10, 0.01f));

            trades.get(3).add(new RandomItemToEmerald(
                    dyes, UniformInt.of(14, 19),
                            ONE, 16, 10, 0.01f));
            trades.get(3).add(new EmeraldToRandomItem(
                                    constant(2),
                ModUtil.getAllDyedItems(dye -> ResourceLocation.withDefaultNamespace(dye +  "_terracotta")),
                                    constant(4), 12, 10, 0.01f
            ));
            trades.get(3).add(new EmeraldToRandomItem(
                    constant(2),
                ModUtil.getAllDyedItems(dye -> ResourceLocation.withDefaultNamespace(dye +  "_glazed_terracotta")),
                    constant(4), 12, 10, 0.01f
            ));



            trades.get(4).add(new ItemToEmerald(
                    Items.CHARCOAL,
                    UniformInt.of(6, 9),
                    ONE, 12, 10, 0.01f
            ));
            trades.get(4).add(new ItemToEmerald(
                    Items.PAINTING,
                    constant(8),
                    ONE, 12, 10, 0.01f
            ));
            if (ModList.get().isLoaded(EaselModConstants.FARMERS_DELIGHT)) {
                trades.get(4).add(new EmeraldToRandomItem(
                        constant(2),
                        ModUtil.getAllDyedItems(dye -> EaselModConstants.farmersDelight(dye + "_hanging_canvas_sign")),
                        ONE, 12, 10, 0.01f
                ));
            }
            if (ModList.get().isLoaded(EaselModConstants.CLAYWORKS)) {
                trades.get(4).add(new EmeraldToRandomItem(
                        ONE,
                        ModUtil.getAllDyedItems(dye -> EaselModConstants.clayworks(dye + "_decorated_pot")),
                        ONE, 12, 10, 0.01f
                ));
            }
            if (ModList.get().isLoaded(EaselModConstants.CHALK)) {
                trades.get(4).add(new EmeraldToRandomItem(
                        ONE,
                        ModUtil.getAllDyedItems(dye -> EaselModConstants.chalk(dye + "_chalk")),
                        UniformInt.of(1, 1), 12, 10, 0.01f
                ));
            }


            trades.get(5).add(new EmeraldToPainting(EaselModPaintings.CULTURE.get(),
                    UniformInt.of(5, 5), 12, 25, 0.1f));
        }
    }
}
