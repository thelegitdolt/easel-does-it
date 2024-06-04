package com.dolthhaven.easeldoesit.core.registry;

import com.dolthhaven.easeldoesit.common.gui.EaselMenu;
import com.dolthhaven.easeldoesit.common.gui.EaselScreen;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EaselModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, EaselDoesIt.MOD_ID);

    public static final RegistryObject<MenuType<EaselMenu>> EASEL = registerMenuType("easel", EaselMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void registerScreens() {
        MenuScreens.register(EASEL.get(), EaselScreen::new);
    }
}
