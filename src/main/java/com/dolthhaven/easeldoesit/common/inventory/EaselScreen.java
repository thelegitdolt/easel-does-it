package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EaselScreen extends AbstractContainerScreen<EaselMenu> {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/client/gui/screens/inventory/SawmillScreen.java

    private static final ResourceLocation BG_LOCATION = EaselDoesIt.rl("textures/gui/container/easel.png");
    private final int imageWidth, imageHeight; // sides of the gui
    private int leftPos, topPos; // leftmost position of gui


    public EaselScreen(EaselMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.inventoryLabelY = 1000;

        if (Objects.isNull(this.minecraft))
            return;

        Level level = this.minecraft.level;

        if (Objects.isNull(level))
            return;

        // adds a functioning button widget that can is rendered by default
        // i will need to use addWidget() because i need to render the button myself
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics); // render black shading behind background, mowzies mobs
        renderTooltip(graphics, mouseX, mouseY);

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BG_LOCATION);

        graphics.blit(BG_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
