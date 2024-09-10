package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EaselScreen extends AbstractContainerScreen<EaselMenu> {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/client/gui/screens/inventory/SawmillScreen.java
    private static final ResourceLocation BG_LOCATION = EaselDoesIt.rl("textures/gui/container/easel.png");
    private static final int BUTTONS_DIMENSIONS_LONG = 16;
    private static final int BUTTONS_DIMENSIONS_SHORT = 7;
    private static final int HEIGHT_BUTTONS_START_X = 48;
    private static final int HEIGHT_BUTTONS_START_Y = 14;

    private static final int WIDTH_BUTTONS_START_X = 56;
    private static final int WIDTH_BUTTONS_START_Y = 6;


    private static final int PREVIEW_BOX_X_POS = 56;
    private static final int PREVIEW_BOX_Y_POS = 14;


    private final int imageWidth, imageHeight; // sides of the gui
    private int leftPos, topPos; // leftmost position of gui
    private final List<Button> paintingDimensionsButtons = Lists.newArrayList();


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

        addHeightButtons();
        addWidthButtons();

        // adds a functioning button widget that can is rendered by default
        // i will need to use addWidget() because i need to render the button myself
    }

    private void addHeightButtons() {
        for (int i = 1; i <= 4; i++) {
            Button button = Button.builder(Component.empty(), onPressForButton(i * BUTTONS_DIMENSIONS_LONG, getMenu()::setPaintingHeight))
                    .pos(this.leftPos + HEIGHT_BUTTONS_START_X, this.topPos + HEIGHT_BUTTONS_START_Y + BUTTONS_DIMENSIONS_LONG * (i - 1))
                    .size(BUTTONS_DIMENSIONS_SHORT, BUTTONS_DIMENSIONS_LONG)
                    .build();

            addRenderableWidget(button);
            paintingDimensionsButtons.add(button);
        }
    }

    private void addWidthButtons() {
        for (int i = 1; i <= 4; i++) {
            Button button = Button.builder(Component.empty(), onPressForButton(i * BUTTONS_DIMENSIONS_LONG, getMenu()::setPaintingWidth))
                    .pos(this.leftPos + WIDTH_BUTTONS_START_X + BUTTONS_DIMENSIONS_LONG * (i - 1), this.topPos + WIDTH_BUTTONS_START_Y)
                    .size(BUTTONS_DIMENSIONS_LONG, BUTTONS_DIMENSIONS_SHORT)
                    .build();

            addRenderableWidget(button);
            paintingDimensionsButtons.add(button);
        }
    }

    private Button.OnPress onPressForButton(int index, Consumer<Integer> setter) {
        return (button) -> {
            setter.accept(index);
            getMenu().dimensionChanged();
        };
    }

    @Override
    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {
        return super.mouseClicked(p_97748_, p_97749_, p_97750_);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderBackground(graphics); // render black shading behind background, mowzies mobs

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BG_LOCATION);

        graphics.blit(BG_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        PaintingVariant currentPainting = this.menu.getCurrentPainting();
        TextureAtlasSprite currentPaintingSprite = Minecraft.getInstance().getPaintingTextures().get(currentPainting);

        graphics.blit(this.leftPos + PREVIEW_BOX_X_POS, this.topPos + PREVIEW_BOX_Y_POS,
                0, currentPainting.getWidth(), currentPainting.getHeight(), currentPaintingSprite); // draw the current painting
    }

}
