package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.common.network.EaselModPacketListener;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingHeightPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingIndexPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingWidthPacket;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.other.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EaselScreen extends AbstractContainerScreen<EaselMenu> {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/client/gui/screens/inventory/SawmillScreen.java
    private static final ResourceLocation BG_LOCATION = EaselDoesIt.rl("textures/gui/container/easel.png");

    private final int imageWidth, imageHeight; // sides of the gui
    private int leftPos, topPos; // leftmost position of gui
    private final EaselWidthButton[] paintingWidthButtons = new EaselWidthButton[4];
    private final EaselHeightsButton[] paintingHeightButtons = new EaselHeightsButton[4];
    private final EaselPickerButton[] paintingPickers = new EaselPickerButton[2];


    public EaselScreen(EaselMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);

        this.menu.registerUpdateListener(this::containerChanged);

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

            button.active = false;
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

            button.active = false;
            addRenderableWidget(button);
            paintingHeightButtons[i - 1] = button;
        }
    }

    private void containerChanged() {
        if (this.getMenu().inputSlot.getItem().is(Items.PAINTING)) {
            for (EaselDimensionsButton button : this.paintingHeightButtons) {
                button.active = true;
            }
            for (EaselDimensionsButton button : this.paintingWidthButtons) {
                button.active = true;
            }
        }
        else {
            for (EaselDimensionsButton button : this.paintingHeightButtons) {
                button.active = false;
            }
            for (EaselDimensionsButton button : this.paintingWidthButtons) {
                button.active = false;
            }
        }
    }

    private void addPickers() {
        EaselPickerButton topPicker = new EaselPickerButton(this.leftPos + PICKER_X, this.topPos + PICKER_TOP_Y, this) {
            @Override
            int affectIndex(int oldIndex) {
                return oldIndex - 1;
            }

            @Override
            int[] getTextureAtlasCords() {
                return new int[]{PICKER_TOP_ATLAS_X, PICKER_ATLAS_Y, PICKER_TOP_HOVERED_ATLAS_X, PICKER_ATLAS_Y, PICKER_TOP_INACTIVE_ATLAS_X, PICKER_ATLAS_Y};
            }
        };

        EaselPickerButton bottomPicker = new EaselPickerButton(this.leftPos + PICKER_X, this.topPos + PICKER_BOTTOM_Y, this) {
            @Override
            int affectIndex(int oldIndex) {
                return oldIndex + 1;
            }

            @Override
            int[] getTextureAtlasCords() {
                return new int[]{PICKER_BOTTOM_ATLAS_X, PICKER_ATLAS_Y, PICKER_BOTTOM_HOVERED_ATLAS_X, PICKER_ATLAS_Y, PICKER_BOTTOM_INACTIVE_ATLAS_X, PICKER_ATLAS_Y};
            }
        };

        this.paintingPickers[0] = topPicker;
        this.paintingPickers[1] = bottomPicker;

        this.addRenderableWidget(topPicker);
        this.addRenderableWidget(bottomPicker);
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

        renderPaintingGrid(graphics);
        renderPainting(graphics);
        renderPageManager(graphics);

        graphics.drawString(this.font, menu.getPaintingWidth() + ", " + menu.getPaintingHeight() + " Painting index: " + menu.getPaintingIndex(), 0, 0, 0xffffff);
    }

    private void renderPaintingGrid(GuiGraphics graphics) {
        if (isEaselActive()) {
            graphics.blit(BG_LOCATION,
                    this.leftPos + PREVIEW_BOX_X, this.topPos + PREVIEW_BOX_Y,
                    PREVIEW_BOX_ATLAS_X, PREVIEW_BOX_ATLAS_Y, PREVIEW_BOX_DIMENSIONS, PREVIEW_BOX_DIMENSIONS);
        }
    }

    private void renderPageManager(GuiGraphics graphics) {
        int menuPaintingIndex = getMenu().getPaintingIndex();
        int currentPage = MathUtil.ceil((double) (menuPaintingIndex + 1) / MAX_PAINTINGS_PER_PAGE);

        int numPaintingsInThisPage = Math.min(MAX_PAINTINGS_PER_PAGE, getMenu().getPossiblePaintingsSize() - MAX_PAINTINGS_PER_PAGE * (currentPage - 1));

        for (int[] yPosAndIndex : getPageButtonYPositionsAndRepresentedIndex(currentPage, numPaintingsInThisPage)) {
            int yPos = yPosAndIndex[0];
            int currentIndex = yPosAndIndex[1];

            int dotXLoc, dotYLoc;
            if (currentIndex == menuPaintingIndex)  {
                dotXLoc = PAGE_BUTTON_SELECTED_X;
                dotYLoc = PAGE_BUTTON_SELECTED_Y;
            }
            else {
                dotXLoc = PAGE_BUTTON_X;
                dotYLoc = PAGE_BUTTON_Y;
            }

            graphics.blit(BG_LOCATION,
                    this.leftPos + PAGES_START_X, this.topPos + yPos,
                    dotXLoc, dotYLoc,
                    PAGE_BUTTON_DIMENSIONS, PAGE_BUTTON_DIMENSIONS);

        }
    }

    private List<int[]> getPageButtonYPositionsAndRepresentedIndex(int currentPage, int numPaintingsInThisPage) {
        List<int[]> list = Lists.newArrayList();
        int yPos = (AVAILABLE_PIXELS_PER_PAGE - totalReqPixelsFor(numPaintingsInThisPage)) / 2;
        yPos += 2;
        for (int i = 0; i < numPaintingsInThisPage; i++) {
            list.add(new int[]{yPos + PAGE_BUTTONS_START, (currentPage - 1) * MAX_PAINTINGS_PER_PAGE + i});
            yPos += 6;
        }

        return list;
    }

    private int totalReqPixelsFor(int numPaintingsInPage) {
        return numPaintingsInPage * 6 + 3;
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

    private boolean isEaselActive() {
        return getMenu().inputSlot.getItem().is(Items.PAINTING);
    }

    private void setMenuPaintingWidth(int newWidth) {
        this.menu.dimensionChangedPre();
        this.menu.setPaintingWidth(newWidth);
        this.menu.dimensionChangedPost();

        EaselModPacketListener.sendToServer(new C2SSetEaselPaintingWidthPacket((byte) newWidth));
        updatePickers(this.menu.getPaintingIndex());
    }

    private void setMenuPaintingHeight(int newHeight) {
        this.menu.dimensionChangedPre();
        this.menu.setPaintingHeight(newHeight);
        this.menu.dimensionChangedPost();

        EaselModPacketListener.sendToServer(new C2SSetEaselPaintingHeightPacket((byte) newHeight));
        updatePickers(this.menu.getPaintingIndex());
    }

    private void setMenuIndex(int newIndex) {
        this.menu.setPaintingIndex(newIndex);
        EaselModPacketListener.sendToServer(new C2SSetEaselPaintingIndexPacket((short) newIndex));
        updatePickers(newIndex);
    }

    @OnlyIn(Dist.CLIENT)
    private class EaselWidthButton extends EaselDimensionsButton {
        public EaselWidthButton(int startX, int startY, int index) {
            super(startX, startY, BUTTONS_DIMENSIONS_LONG, BUTTONS_DIMENSIONS_SHORT, index, EaselScreen.this);
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

    private void updatePickers(int newIndex) {
        for (EaselPickerButton button : this.paintingPickers) {
            button.active = button.canBePressed(newIndex);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private class EaselHeightsButton extends EaselDimensionsButton {
        public EaselHeightsButton(int startX, int startY, int index) {
            super(startX, startY, BUTTONS_DIMENSIONS_SHORT, BUTTONS_DIMENSIONS_LONG, index, EaselScreen.this);
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
        public final EaselScreen screen;

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
        public EaselDimensionsButton(int startX, int startY, int width, int height, int index, EaselScreen screen) {
            super(startX, startY, width, height, Component.empty());

            this.index = index;
            this.screen = screen;
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics graphics, int p_282682_, int p_281714_, float p_282542_) {
            if (!this.screen.isEaselActive()) {
                return;
            }

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

    private abstract static class EaselPickerButton extends AbstractButton {
        private final EaselScreen screen;

        public EaselPickerButton(int startX, int startY, EaselScreen screen) {
            super(startX, startY, PICKER_X_DIMENSION, PICKER_Y_DIMENSION, CommonComponents.EMPTY);
            this.screen = screen;
            this.active = false;
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }

        abstract int affectIndex(int oldIndex);

        abstract int[] getTextureAtlasCords();

        @Override
        public void onPress() {
            int paintingIndex = screen.getMenu().getPaintingIndex();
            int newIndex = affectIndex(paintingIndex);

            if (canBePressed(paintingIndex)) {
                screen.setMenuIndex(newIndex);
            }
        }

        public boolean canBePressed(int index) {
            return screen.getMenu().isValidPaintingIndex(affectIndex(index));
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics graphics, int p_282682_, int p_281714_, float p_282542_) {
            if (screen.getMenu().getPossiblePaintingsSize() == 0)
                return;

            int atlasXCord, atlasYCord;

            if (!this.isActive()) {
                atlasXCord = getTextureAtlasCords()[4];
                atlasYCord = getTextureAtlasCords()[5];
            }
            else if (this.isHovered()) {
                atlasXCord = getTextureAtlasCords()[2];
                atlasYCord = getTextureAtlasCords()[3];
            }
            else {
                atlasXCord = getTextureAtlasCords()[0];
                atlasYCord = getTextureAtlasCords()[1];
            }

            graphics.blit(BG_LOCATION,
                    getX(), getY(),
                    atlasXCord, atlasYCord,
                    this.width, this.height
            );
        }
    }

    private static final int MAX_PAINTINGS_PER_PAGE = 8;
    private static final int AVAILABLE_PIXELS_PER_PAGE = 51;
    private static final int PAGES_START_X = 126;
    private static final int PAGE_BUTTON_X = 181;
    private static final int PAGE_BUTTON_Y = 23;
    private static final int PAGE_BUTTON_SELECTED_X = 176;
    private static final int PAGE_BUTTON_SELECTED_Y = 23;
    private static final int PAGE_BUTTON_DIMENSIONS = 5;
    private static final int PAGE_BUTTONS_START = 21;

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
    private static final int PICKER_ATLAS_Y = 28;
    private static final int PICKER_BOTTOM_ATLAS_X = 176;
    private static final int PICKER_TOP_ATLAS_X = 187;
    private static final int PICKER_BOTTOM_HOVERED_ATLAS_X = 198;
    private static final int PICKER_TOP_HOVERED_ATLAS_X = 209;
    private static final int PICKER_BOTTOM_INACTIVE_ATLAS_X = 220;
    private static final int PICKER_TOP_INACTIVE_ATLAS_X = 231;

    private static final int PREVIEW_BOX_X = 56;
    private static final int PREVIEW_BOX_Y = 14;
    private static final int PREVIEW_BOX_ATLAS_X = 176;
    private static final int PREVIEW_BOX_ATLAS_Y = 35;
    private static final int PREVIEW_BOX_DIMENSIONS = 64;

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
}
