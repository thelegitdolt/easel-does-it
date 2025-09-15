package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import com.dolthhaven.easeldoesit.common.inventory.EaselScreen;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(value = Dist.CLIENT, modid = EaselDoesIt.MOD_ID)
public class EaselModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, EaselDoesIt.MOD_ID);

    public static final Supplier<MenuType<EaselMenu>> EASEL_MENU = MENUS
            .register("easel_menu", () -> new MenuType<>(EaselMenu::new, FeatureFlags.VANILLA_SET));

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(EASEL_MENU.get(), EaselScreen::new);
    }
}
