package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselDimensionsPacket;
import com.dolthhaven.easeldoesit.common.network.packets.C2SSetEaselPaintingIndexPacket;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.other.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
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
                    this.leftPos + WIDTH_BUTTONS_START_GUI.x() + BUTTONS_DIMENSIONS_LONG * (i - 1),
                    this.topPos + WIDTH_BUTTONS_START_GUI.y(),
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
            List<Vector2i> atlasCords() {
                return List.of(PICKER_TOP_INACTIVE_UV, PICKER_TOP_HOVERED_UV, PICKER_TOP_UV);
            }
        };

        EaselPickerButton bottomPicker = new EaselPickerButton(this.leftPos + PICKER_X, this.topPos + PICKER_BOTTOM_Y, this) {
            @Override
            int affectIndex(int oldIndex) {
                return oldIndex + 1;
            }

            @Override
            List<Vector2i> atlasCords() {
                return List.of(PICKER_BOTTOM_INACTIVE_UV, PICKER_BOTTOM_HOVERED_UV, PICKER_BOTTOM_UV);
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (clickPageManager(mouseX, mouseY)) {
            return true;
        } return super.mouseClicked(mouseX, mouseY, button);
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
                    this.leftPos + PREVIEW_BOX_GUI.x(), this.topPos + PREVIEW_BOX_GUI.y(),
                    PREVIEW_BOX_UV.x(), PREVIEW_BOX_UV.y(), PREVIEW_BOX_SIZE, PREVIEW_BOX_SIZE);
        }
    }

    private void renderPageManager(GuiGraphics graphics) {
        if (!isEaselActive() || !menu.isLegalDimensions())
            return;

        int index = getMenu().getPaintingIndex();

        for (Vector2i coord : pageManagerInfo()) {
            int currentIndex = coord.x;
            int yPos = coord.y;

            boolean selected = currentIndex == index;
            Vector2i uv = selected ? PAGE_BUTTON_SELECTED : PAGE_BUTTON;

            graphics.blit(BG_LOCATION,
                    this.leftPos + PAGES_START_X, this.topPos + yPos,
                    uv.x(), uv.y(),
                    PAGE_BUTTON_SIZE, PAGE_BUTTON_SIZE);
        }
    }

    private List<Vector2i> pageManagerInfo() {
        int menuPaintingIndex = getMenu().getPaintingIndex();
        int currentPage = Mth.ceil((double) (menuPaintingIndex + 1) / PAINTINGS_PER_PAGE);

        int numPaintings = Math.min(PAINTINGS_PER_PAGE, getMenu().getPossiblePaintingsSize() - PAINTINGS_PER_PAGE * (currentPage - 1));

        List<Vector2i> list = new ArrayList<>();
        int yPos = (PIXELS_PER_PAGE - totalReqPixelsFor(numPaintings)) / 2;
        yPos += 2;
        for (int i = 0; i < numPaintings; i++) {
            list.add(new Vector2i((currentPage - 1) * PAINTINGS_PER_PAGE + i, yPos + PAGE_BUTTONS_START));
            yPos += 6;
        }

        return list;
    }

    private boolean clickPageManager(double mouseX, double mouseY) {
        for (Vector2i buttons : pageManagerInfo()) {
            int start = buttons.y();
            boolean inXRange = MathUtil.isBetween((int) mouseX, leftPos + PAGES_START_X, leftPos + PAGES_START_X + PAGE_BUTTON_SIZE);
            boolean inYRange = MathUtil.isBetween((int) mouseY, topPos + buttons.y(), topPos + buttons.y() + PAGE_BUTTON_SIZE);
            boolean notIdentityTransformation = buttons.x() != getMenu().getPaintingIndex();
            if (inXRange && inYRange && notIdentityTransformation) {
                setMenuIndex(buttons.x());
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        } return false;
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
            if (painting.width() > 4 || painting.height() > 4) return;

            TextureAtlasSprite paintingSprite = Minecraft.getInstance().getPaintingTextures().get(painting);
            graphics.blit(this.leftPos + PREVIEW_BOX_GUI.x(), this.topPos + PREVIEW_BOX_GUI.y(),
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
        protected List<Vector2i> atlasCords() {
            return List.of(WIDTH_BUTTON_CLICKED, WIDTH_BUTTON_NOT_CLICKED, WIDTH_BUTTON_HOVERED);
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
        protected List<Vector2i> atlasCords() {
            return List.of(HEIGHT_BUTTON_CLICKED, HEIGHT_BUTTON_NOT_CLICKED, HEIGHT_BUTTON_HOVERED);
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

        protected abstract List<Vector2i> atlasCords();

        protected abstract int getRelevantDimension();

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
            Vector2i uv = MathUtil.decisionTree(atlasCords(),paintingDimension >= this.index, !this.isHovered());

            graphics.blit(BG_LOCATION, getX(), getY(), uv.x, uv.y, this.width, this.height);
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

        ///  inactive, hovered, normal
        abstract List<Vector2i> atlasCords();

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
            if (!screen.isEaselActive() || screen.getMenu().getPossiblePaintingsSize() == 0)
                return;

            Vector2i uv = MathUtil.decisionTree(atlasCords(), !this.isActive(), this.isHovered());

            graphics.blit(BG_LOCATION,
                    this.getX(), this.getY(),
                    uv.x, uv.y,
                    this.width, this.height
            );
        }
    }



    private static final int PAINTINGS_PER_PAGE = 8;
    private static final int PIXELS_PER_PAGE = 51;
    private static final int PREVIEW_BOX_SIZE = 64;
    private static final int PAGES_START_X = 126;
    private static final Vector2i PAGE_BUTTON = new Vector2i(181, 23);
    private static final Vector2i PAGE_BUTTON_SELECTED = new Vector2i(176, 23);
    private static final int PAGE_BUTTON_SIZE = 5;
    private static final int PAGE_BUTTONS_START = 21;

    private static final int BUTTONS_DIMENSIONS_LONG = 16;
    private static final int BUTTONS_DIMENSIONS_SHORT = 7;
    private static final int HEIGHT_BUTTONS_START_X = 48;
    private static final int HEIGHT_BUTTONS_START_Y = 14;

    private static final Vector2i WIDTH_BUTTONS_START_GUI = new Vector2i(56, 6);

    private static final int PICKER_X = 123;
    private static final int PICKER_TOP_Y = 14;
    private static final int PICKER_BOTTOM_Y = 72;
    private static final int PICKER_X_DIMENSION = 11;
    private static final int PICKER_Y_DIMENSION = 7;
    private static final int PICKER_ATLAS_Y = 28;
    private static final Vector2i PICKER_TOP_UV = new Vector2i(187, 28);
    private static final Vector2i PICKER_TOP_HOVERED_UV = new Vector2i(209, 28);
    private static final Vector2i PICKER_TOP_INACTIVE_UV = new Vector2i(231, 28);
    private static final Vector2i PICKER_BOTTOM_UV = new Vector2i(176, 28);
    private static final Vector2i PICKER_BOTTOM_HOVERED_UV = new Vector2i(198, 28);
    private static final Vector2i PICKER_BOTTOM_INACTIVE_UV = new Vector2i(220, 28);

    private static final Vector2i PREVIEW_BOX_GUI = new Vector2i(56, 14);
    private static final Vector2i PREVIEW_BOX_UV = new Vector2i(176, 35);

    private static final Vector2i WIDTH_BUTTON_NOT_CLICKED = new Vector2i(183, 0);
    private static final Vector2i WIDTH_BUTTON_CLICKED = new Vector2i(176, 16);
    private static final Vector2i WIDTH_BUTTON_HOVERED = new Vector2i(206, 0);
    private static final Vector2i HEIGHT_BUTTON_NOT_CLICKED = new Vector2i(192, 7);
    private static final Vector2i HEIGHT_BUTTON_CLICKED = new Vector2i(176, 0);
    private static final Vector2i HEIGHT_BUTTON_HOVERED = new Vector2i(199, 0);
}
