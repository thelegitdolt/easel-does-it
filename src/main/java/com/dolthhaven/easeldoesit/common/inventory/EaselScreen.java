package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselDimensionsPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingIndexPacket;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class EaselScreen extends AbstractContainerScreen<EaselMenu> {
    private static final ResourceLocation BG_LOCATION = EaselDoesIt.rl("textures/gui/container/easel.png");

    private final int imageWidth, imageHeight; // sides of the gui
    private int leftPos, topPos; // leftmost position of gui
    private float untilNextScroll = 0;
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

        topPicker.active = false;
        bottomPicker.active = false;

        this.paintingPickers[0] = topPicker;
        this.paintingPickers[1] = bottomPicker;

        this.addRenderableWidget(topPicker);
        this.addRenderableWidget(bottomPicker);
    }

    private void containerChanged() {
        if (isEaselActive()) {
            for (EaselDimensionsButton button : this.paintingHeightButtons) {
                button.active = true;
            }
            for (EaselDimensionsButton button : this.paintingWidthButtons) {
                button.active = true;
            }
            for (EaselPickerButton button : this.paintingPickers) {
                if (button.canBePressed(menu.getPaintingIndex())) button.active = true;
            }
        }
        else {
            for (EaselDimensionsButton button : this.paintingHeightButtons) {
                button.active = false;
            }
            for (EaselDimensionsButton button : this.paintingWidthButtons) {
                button.active = false;
            }
            for (EaselPickerButton button : this.paintingPickers) {
                button.active = false;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        double potentialIndex = subtractInputFromScroll(scrollY);
        float remainders = (float) potentialIndex - Mth.floor(potentialIndex);
        this.untilNextScroll += remainders;

        if (untilNextScroll > 1) {
            untilNextScroll--;
            potentialIndex++;
        }

        this.setMenuIndex(Mth.floor(potentialIndex));

        return true;
    }

    protected double subtractInputFromScroll(double pInput) {
        Double2DoubleFunction scrollFunc = dub -> {
            int sign = dub < 0 ? -1 : 1;
            double absDub = Math.abs(dub);
            double thresh = 3.2d;

            if (absDub < thresh)
                return thresh * sign;
            else
                return (thresh + thresh + absDub) / 3 * sign;
        };

        pInput = scrollFunc.apply(pInput);
        double multiplier = 1.1;
        double divide =  this.menu.getPossiblePaintingsSize() * multiplier;

        return Mth.clamp((double) menu.getPaintingIndex() - (pInput / divide),
                0, menu.getPossiblePaintingsSize() - 1);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BG_LOCATION);

        graphics.blit(BG_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        renderPaintingGrid(graphics);
        renderPainting(graphics);
        renderPageManager(graphics);
    }

    private void renderPaintingGrid(GuiGraphics graphics) {
        if (isEaselActive()) {
            graphics.blit(BG_LOCATION,
                    this.leftPos + PREVIEW_BOX_X, this.topPos + PREVIEW_BOX_Y,
                    PREVIEW_BOX_ATLAS_X, PREVIEW_BOX_ATLAS_Y, PREVIEW_BOX_DIMENSIONS, PREVIEW_BOX_DIMENSIONS);
        }
    }

    private void renderPageManager(GuiGraphics graphics) {
        if (!isEaselActive() || !menu.isLegalDimensions())
            return;

        int menuPaintingIndex = getMenu().getPaintingIndex();
        int currentPage = Mth.ceil((double) (menuPaintingIndex + 1) / MAX_PAINTINGS_PER_PAGE);

        int numPaintingsInThisPage = Math.min(MAX_PAINTINGS_PER_PAGE, getMenu().getPossiblePaintingsSize() - MAX_PAINTINGS_PER_PAGE * (currentPage - 1));

        for (Vector2i coord : getPageInfo(currentPage, numPaintingsInThisPage)) {
            int currentIndex = coord.x;
            int yPos = coord.y;

            boolean selected = currentIndex == menuPaintingIndex;
            int dotXLoc = selected ? PAGE_BUTTON_SELECTED_X : PAGE_BUTTON_X;
            int dotYLoc = selected ? PAGE_BUTTON_SELECTED_Y : PAGE_BUTTON_Y;

            graphics.blit(BG_LOCATION,
                    this.leftPos + PAGES_START_X, this.topPos + yPos,
                    dotXLoc, dotYLoc,
                    PAGE_BUTTON_DIMENSIONS, PAGE_BUTTON_DIMENSIONS);
        }
    }

    private List<Vector2i> getPageInfo(int currentPage, int numPaintings) {
        List<Vector2i> list = Lists.newArrayList();
        int yPos = (AVAILABLE_PIXELS_PER_PAGE - totalReqPixelsFor(numPaintings)) / 2;
        yPos += 2;
        for (int i = 0; i < numPaintings; i++) {
            list.add(new Vector2i((currentPage - 1) * MAX_PAINTINGS_PER_PAGE + i, yPos + PAGE_BUTTONS_START));
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
        if (!isEaselActive() || !menu.isLegalIndex() || !menu.isLegalDimensions()) return;

        this.menu.getCurrentPainting().ifPresent(painting -> {
            TextureAtlasSprite paintingSprite = Minecraft.getInstance().getPaintingTextures().get(painting);
            graphics.blit(this.leftPos + PREVIEW_BOX_X, this.topPos + PREVIEW_BOX_Y,
                    0, painting.width() * 16, painting.height() * 16, paintingSprite);
        });
    }

    private boolean isEaselActive() {
        return getMenu().inputSlot.getItem().is(Items.PAINTING);
    }

    private void setMenuPaintingWidth(int newWidth) {
        this.menu.setPaintingWidth(newWidth);

        PacketDistributor.sendToServer(new C2SSetEaselDimensionsPacket(newWidth, menu.getPaintingHeight()));
        updatePickers(this.menu.getPaintingIndex());
    }

    private void setMenuPaintingHeight(int newHeight) {
        this.menu.setPaintingHeight(newHeight);

        PacketDistributor.sendToServer(new C2SSetEaselDimensionsPacket(menu.getPaintingWidth(), newHeight));
        updatePickers(this.menu.getPaintingIndex());
    }

    private void setMenuIndex(int newIndex) {
        this.menu.setPaintingIndex(newIndex);
        PacketDistributor.sendToServer(new C2SSetEaselPaintingIndexPacket(newIndex));
        updatePickers(newIndex);
    }

    private void updatePickers(int newIndex) {
        for (EaselPickerButton button : this.paintingPickers) {
            button.active = button.canBePressed(newIndex);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private class EaselWidthButton extends EaselDimensionsButton {
        public EaselWidthButton(int startX, int startY, int index) {
            super(startX, startY, BUTTONS_DIMENSIONS_LONG, BUTTONS_DIMENSIONS_SHORT, index, EaselScreen.this);
        }

        @Override
        public void onPress() {
            EaselScreen.this.setMenuPaintingWidth(index);
        }

        @Override
        protected int[] atlasCords() {
            return new int[]{WIDTH_BUTTON_CLICKED_X, WIDTH_BUTTON_CLICKED_Y, WIDTH_BUTTON_NOT_CLICKED_X, WIDTH_BUTTON_NOT_CLICKED_ATLAS_CORDS_Y, WIDTH_BUTTON_HOVERED_X, WIDTH_BUTTON_HOVERED_Y};
        }

        @Override
        protected int getRelevantDimension() {
            return EaselScreen.this.getMenu().getPaintingWidth();
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
            EaselScreen.this.setMenuPaintingHeight(index);

//            for (int i = 1; i <= 4; i += 1) {
//                getWidthButtonOfIndex(i).active = !PaintingUtil.getAllPaintingsOfDimensions(i * 16, newHeights).isEmpty();
//            }

            EaselScreen.this.getMenu().dimensionChangedPost();
        }

        @Override
        protected int[] atlasCords() {
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
         * @return int[] of (clicked_x, clicked_y, not clicked_x, not clicked_y, hovered_x, hovered_y)
         */
        protected abstract List<Vector2i> atlasCords();

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

            int paintingDimension = getRelevantDimension();
            int xPos = decisionTree(atlasCords(), Vector2i::x, paintingDimension >= this.index, !this.isHovered());
            int yPos = decisionTree(atlasCords(), Vector2i::y, paintingDimension >= this.index, !this.isHovered());

            graphics.blit(BG_LOCATION, getX(), getY(), xPos, yPos, this.width, this.height);
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }

    @OnlyIn(Dist.CLIENT)
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
            return screen.getMenu().isLegalIndex(affectIndex(index));
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics graphics, int p_282682_, int p_281714_, float p_282542_) {
            if (!screen.isEaselActive())
                return;

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

    private static int decisionTree(List<Vector2i> choices, Function<Vector2i, Integer> operation, boolean... branches) {
        int index = -1;
        for (boolean branch : branches) {
            index++;
            if (branch) break;
        }
        return operation.apply(choices.get(index));
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

    private static final Vector2i WIDTH_BUTTON_NOT_CLICKED = new Vector2i(183, 0);
    private static final int HEIGHT_BUTTON_NOT_CLICKED_ATLAS_CORDS_X = 192;
    private static final int HEIGHT_BUTTON_NOT_CLICKED_ATLAS_CORDS_Y = 7;
    private static final Vector2i WIDTH_BUTTON_CLICKED = new Vector2i(176, 16);
    private static final int HEIGHT_BUTTON_CLICKED_ATLAS_CORDS_X = 176;
    private static final int HEIGHT_BUTTON_CLICKED_ATLAS_CORDS_Y = 0;
    private static final Vector2i WIDTH_BUTTON_HOVERED = new Vector2i(206, 0);
    private static final int HEIGHT_BUTTON_HOVERED_X = 199;
    private static final int HEIGHT_BUTTON_HOVERED_Y = 0;
}
