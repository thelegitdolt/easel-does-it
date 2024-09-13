package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.common.network.EaselModPacketListener;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingHeightPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingIndexPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingWidthPacket;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class EaselScreen extends AbstractContainerScreen<EaselMenu> {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/client/gui/screens/inventory/SawmillScreen.java
    private static final ResourceLocation BG_LOCATION = EaselDoesIt.rl("textures/gui/container/easel.png");

    private final int imageWidth, imageHeight; // sides of the gui
    private int leftPos, topPos; // leftmost position of gui
    private final EaselWidthButton[] paintingWidthButtons = new EaselWidthButton[4];
    private final EaselHeightsButton[] paintingHeightButtons = new EaselHeightsButton[4];

    private final List<Button> paintingPickers = Lists.newArrayList();


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

        // remove "inventory" label (wow)
        this.inventoryLabelY = 1000;

        // buttons
        addHeightButtons();
        addWidthButtons();
        addPickers();
    }

    private void addWidthButtons() {
        for (int i = 1; i <= 4; i++) {
            EaselWidthButton button = new EaselWidthButton(
                    this.leftPos + WIDTH_BUTTONS_START_X + BUTTONS_DIMENSIONS_LONG * (i - 1),
                    this.topPos + WIDTH_BUTTONS_START_Y,
                    i);

            addRenderableWidget(button);
            paintingWidthButtons[i - 1] = button;
        }
    }

    private void addHeightButtons() {
        for (int i = 1; i <= 4; i++) {
            EaselHeightsButton button = new EaselHeightsButton(
                    this.leftPos + HEIGHT_BUTTONS_START_X,
                    this.topPos + HEIGHT_BUTTONS_START_Y + BUTTONS_DIMENSIONS_LONG * (i - 1),
                    i);

            addRenderableWidget(button);
            paintingHeightButtons[i - 1] = button;
        }
    }

    private void addPickers() {
        Button topPicker = Button.builder(Component.empty(), onPressForPickers((i) -> i - 1))
                .pos(this.leftPos + PICKER_X, this.topPos + PICKER_TOP_Y)
                .size(PICKER_X_DIMENSION, PICKER_Y_DIMENSION)
                .build();

        Button bottomPicker = Button.builder(Component.empty(), onPressForPickers((i) -> i + 1))
                .pos(this.leftPos + PICKER_X, this.topPos + PICKER_BOTTOM_Y)
                .size(PICKER_X_DIMENSION, PICKER_Y_DIMENSION)
                .build();

        this.paintingPickers.add(topPicker);
        this.paintingPickers.add(bottomPicker);

        this.addWidget(topPicker);
        this.addWidget(bottomPicker);
    }

    private Button.OnPress onPressForPickers(Function<Integer, Integer> indexFunction) {
        return (button) -> {
            int newIndex = indexFunction.apply(this.menu.getPaintingIndex());
            if (this.menu.isValidPaintingIndex(newIndex)) {
                setMenuIndex(newIndex);
            }
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseID) {
        return super.mouseClicked(mouseX, mouseY, mouseID);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseID) {
        return super.mouseReleased(mouseX, mouseY, mouseID);
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

        renderPainting(graphics);

        graphics.drawString(this.font, menu.getPaintingWidth() + ", " + menu.getPaintingHeight() + " Painting index: " + menu.getPaintingIndex(), 0, 0, 0xffffff);
    }

    /**
     * handles the drawing of the paintings.
     */
    private void renderPainting(GuiGraphics graphics) {
        // draw nothing if one width or height is 0
        if (this.menu.getPaintingWidth() == 0 || this.menu.getPaintingWidth() == 0) return;

        if (this.menu.getPossiblePaintingsSize() == 0) return;

        PaintingVariant currentPainting = this.menu.getCurrentPainting();
        TextureAtlasSprite currentPaintingSprite = Minecraft.getInstance().getPaintingTextures().get(currentPainting);

        graphics.blit(this.leftPos + PREVIEW_BOX_X, this.topPos + PREVIEW_BOX_Y,
                0, currentPainting.getWidth(), currentPainting.getHeight(), currentPaintingSprite); // draw the current painting
    }

    private void setMenuPaintingWidth(int newWidth) {
        this.menu.setPaintingWidth(newWidth);
        EaselModPacketListener.sendToServer(new C2SSetEaselPaintingWidthPacket((byte) newWidth));
    }

    private void setMenuPaintingHeight(int newHeight) {
        this.menu.setPaintingHeight(newHeight);
        EaselModPacketListener.sendToServer(new C2SSetEaselPaintingHeightPacket((byte) newHeight));
    }

    private void setMenuIndex(int newIndex) {
        this.menu.setPaintingIndex(newIndex);
        EaselModPacketListener.sendToServer(new C2SSetEaselPaintingIndexPacket((short) newIndex));
    }

    @OnlyIn(Dist.CLIENT)
    private class EaselWidthButton extends EaselDimensionsButton {
        public EaselWidthButton(int startX, int startY, int index) {
            super(startX, startY, BUTTONS_DIMENSIONS_LONG, BUTTONS_DIMENSIONS_SHORT, index);
        }

        @Override
        public void onPress() {
            EaselScreen.this.getMenu().dimensionChangedPre();
            // !!!!!!!!!!!!!!!!
            int newWidth = index * 16;
            EaselScreen.this.setMenuPaintingWidth(newWidth);

//            for (int i = 1; i <= 4; i += 1) {
//                getHeightButtonOfIndex(i).active = !PaintingUtil.getAllPaintingsOfDimensions(newWidth, i * 16).isEmpty();
//            }

            EaselScreen.this.getMenu().dimensionChangedPost();
        }

        @Override
        protected int[] getAtlasPositionsForButtons() {
            return new int[]{WIDTH_BUTTON_CLICKED_ATLAS_CORDS_X, WIDTH_BUTTON_CLICKED_ATLAS_CORDS_Y, WIDTH_BUTTON_NOT_CLICKED_ATLAS_CORDS_X, WIDTH_BUTTON_NOT_CLICKED_ATLAS_CORDS_Y, WIDTH_BUTTON_HOVERED_X, WIDTH_BUTTON_HOVERED_Y};
        }

        @Override
        protected int getRelevantDimension() {
            return EaselScreen.this.getMenu().getPaintingWidth();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private class EaselHeightsButton extends EaselDimensionsButton {
        public EaselHeightsButton(int startX, int startY, int index) {
            super(startX, startY, BUTTONS_DIMENSIONS_SHORT, BUTTONS_DIMENSIONS_LONG, index);
        }

        @Override
        public void onPress() {
            EaselScreen.this.getMenu().dimensionChangedPre();
            int newHeights = index * 16;
            EaselScreen.this.setMenuPaintingHeight(newHeights);

//            for (int i = 1; i <= 4; i += 1) {
//                getWidthButtonOfIndex(i).active = !PaintingUtil.getAllPaintingsOfDimensions(i * 16, newHeights).isEmpty();
//            }

            EaselScreen.this.getMenu().dimensionChangedPost();
        }

        @Override
        protected int[] getAtlasPositionsForButtons() {
            return new int[]{HEIGHT_BUTTON_CLICKED_ATLAS_CORDS_X, HEIGHT_BUTTON_CLICKED_ATLAS_CORDS_Y, HEIGHT_BUTTON_NOT_CLICKED_ATLAS_CORDS_X, HEIGHT_BUTTON_NOT_CLICKED_ATLAS_CORDS_Y, HEIGHT_BUTTON_HOVERED_X, HEIGHT_BUTTON_HOVERED_Y};
        }

        @Override
        protected int getRelevantDimension() {
            return EaselScreen.this.getMenu().getPaintingHeight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private abstract static class EaselDimensionsButton extends AbstractButton {
        public final int index;

        /**
         * The position of the button textures on the easel gui atlas sprite.
         * @return int[] of (clicked_x, clicked_y, not clicked_x, not clicked_y, hovered_x, hovered_y)
         */
        protected abstract int[] getAtlasPositionsForButtons();

        /**
         * @return the value of the relevant dimension
         * If height it should return the size of the height
         */
        protected abstract int getRelevantDimension();

        /**
         * INDEX IS A NUMBER FROM 1 to 4, not the pixel size of teh painting. IF YOU USE THE PIXEL SIZE OF THE PAINTING
         * YOU WILL BE LOGGED, IT WILL PROBABLY CRASH THE GAME
         */
        public EaselDimensionsButton(int startX, int startY, int width, int height, int index) {
            super(startX, startY, width, height, Component.empty());

            // crash the game if dolt is a dumbass
            if (index == 16 || index == 32 || index == 64 || index == 48) {
                for (int i = 0; i < 20000; i++) {
                    EaselDoesIt.log("YOU ARE A DUMBASS A DUMBASS YOU DUMB FUCK");
                }
            }

            this.index = index;
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics graphics, int p_282682_, int p_281714_, float p_282542_) {
            int buttonToRenderX, buttonToRenderY;
            int paintingDimension = getRelevantDimension() / 16;

            if (paintingDimension >= this.index) {
                buttonToRenderX = getAtlasPositionsForButtons()[0];
                buttonToRenderY = getAtlasPositionsForButtons()[1];
            }
            else if (!this.isHovered()){
                buttonToRenderX = getAtlasPositionsForButtons()[2];
                buttonToRenderY = getAtlasPositionsForButtons()[3];
            }
            else {
                buttonToRenderX = getAtlasPositionsForButtons()[4];
                buttonToRenderY = getAtlasPositionsForButtons()[5];
            }

            graphics.blit(BG_LOCATION,
                    getX(), getY(),
                    buttonToRenderX, buttonToRenderY,
                    this.width, this.height
            );
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }

    private static final int BUTTONS_DIMENSIONS_LONG = 16;
    private static final int BUTTONS_DIMENSIONS_SHORT = 7;
    private static final int HEIGHT_BUTTONS_START_X = 48;
    private static final int HEIGHT_BUTTONS_START_Y = 14;

    private static final int WIDTH_BUTTONS_START_X = 56;
    private static final int WIDTH_BUTTONS_START_Y = 6;

    private static final int PICKER_X = 123;
    private static final int PICKER_TOP_Y = 14;
    private static final int PICKER_BOTTOM_Y = 72;
    private static final int PICKER_X_DIMENSION = 11;
    private static final int PICKER_Y_DIMENSION = 7;

    private static final int PREVIEW_BOX_X = 56;
    private static final int PREVIEW_BOX_Y = 14;

    private static final int WIDTH_BUTTON_NOT_CLICKED_ATLAS_CORDS_X = 183;
    private static final int WIDTH_BUTTON_NOT_CLICKED_ATLAS_CORDS_Y = 0;
    private static final int HEIGHT_BUTTON_NOT_CLICKED_ATLAS_CORDS_X = 192;
    private static final int HEIGHT_BUTTON_NOT_CLICKED_ATLAS_CORDS_Y = 7;
    private static final int WIDTH_BUTTON_CLICKED_ATLAS_CORDS_X = 176;
    private static final int WIDTH_BUTTON_CLICKED_ATLAS_CORDS_Y = 16;
    private static final int HEIGHT_BUTTON_CLICKED_ATLAS_CORDS_X = 176;
    private static final int HEIGHT_BUTTON_CLICKED_ATLAS_CORDS_Y = 0;
    private static final int WIDTH_BUTTON_HOVERED_X = 206;
    private static final int WIDTH_BUTTON_HOVERED_Y = 0;
    private static final int HEIGHT_BUTTON_HOVERED_X = 199;
    private static final int HEIGHT_BUTTON_HOVERED_Y = 0;
//    private static final int DISTANCE_TO_UNHOVERED_COUNTERPART = ;
}
