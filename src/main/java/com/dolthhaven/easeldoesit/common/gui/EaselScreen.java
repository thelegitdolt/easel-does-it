package com.dolthhaven.easeldoesit.common.gui;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class EaselScreen extends AbstractContainerScreen<EaselMenu> {
    private static final ResourceLocation TEXTURE = EaselDoesIt.rl("textures/gui/container/easel.png");

    public EaselScreen(EaselMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int beginX = (width - imageWidth) / 2;
        int beginY = (width - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, beginX, beginY, 0, 0, imageWidth, imageHeight);
    }
}
