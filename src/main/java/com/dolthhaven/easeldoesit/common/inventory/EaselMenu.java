package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.core.registry.EaselModMenuTypes;
import com.dolthhaven.easeldoesit.other.util.MathUtil;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class EaselMenu extends AbstractContainerMenu {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/common/inventory/SawmillMenu.java
    private static final int MIN_DIMENSION = 0;
    private static final int MAX_DIMENSION = 64;

    private final ContainerLevelAccess access;
    // slots
    Runnable slotUpdateListener = () -> {
    };
    final Slot inputSlot;
    final Slot resultSlot;
    public final Container inputContainer = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            EaselMenu.this.slotsChanged(this);
            EaselMenu.this.slotUpdateListener.run();
        }
    };
    final ResultContainer resultContainer = new ResultContainer();

    private final DataSlot paintingIndex = DataSlot.standalone();
    private final DataSlot paintingHeight = DataSlot.standalone();
    private final DataSlot paintingWidth = DataSlot.standalone();
    private final DataSlot[] savedPaintingsInEachDimension = new DataSlot[16];
    private List<PaintingVariant> possiblePaintings = Lists.newArrayList();

    public EaselMenu(int id, Inventory inv) {
        this(id, inv, ContainerLevelAccess.NULL);
    }

    public EaselMenu(int id, Inventory inv, final ContainerLevelAccess access) {
        super(EaselModMenuTypes.EASEL_MENU.get(), id);
        this.access = access;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.inputSlot = this.addSlot(new Slot(this.inputContainer, 0, 15, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.PAINTING);
            }
        });
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                EaselMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack input = EaselMenu.this.inputSlot.remove(1);
                if (!input.isEmpty()) {
                    EaselMenu.this.createResult();
                }
                super.onTake(player, stack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(EaselMenu.this.inputSlot.getItem());
            }
        });
        // add slots on the thing


        // initialize all 16 data slots and stuff
        for (int i = 0; i < 16; i++) {
            DataSlot data = DataSlot.standalone();
            data.set(0);
            savedPaintingsInEachDimension[i] = (data);
        }


        // menu defaults to 0 when first opened
        setPaintingWidth(0);
        setPaintingHeight(0);

        setPaintingIndex(0);
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        super.slotsChanged(container);

        if (container == this.inputContainer) {
            createResult();
        }
    }

    private void createResult() {
        if (this.inputSlot.getItem().is(Items.PAINTING) && isValidPaintingIndex(getPaintingIndex())) {
            PaintingVariant variant = getCurrentPainting();
            ItemStack stack = PaintingUtil.getStackFromPainting(variant);
            this.resultSlot.set(stack);
        }
        else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    /**
     * Method ran BEFORE you change the painting dimensions
     * It will:
     * - save the current painting to the list
     */
    public void dimensionChangedPre() {
        savePaintingForCurrentDimension();
    }

    /**
     * Method ran AFTER you change the painting dimensions
     * It will:
     * - Reset possible paintings
     */
    public void dimensionChangedPost() {
        ItemStack inputStack = this.inputSlot.getItem();
        setPossiblePaintings(PaintingUtil.getAllPaintingsOfDimensions(getPaintingWidth(), getPaintingHeight()));

        // if this exact dimension has been visited before then we save the progress, setting it to that last visited painting.
        // if it hasn't then it should be set to 0
        int newIndex = getIndexFromPaintingCoords();
        setPaintingIndex(newIndex);

        if (inputStack.is(Items.PAINTING)) {
            createResult();
        }
    }

    /**
     * TAKES IN A PAINTING COORD AND GETS THE NUMBER WHICH THE PAINTING INDEX SHOULD BE SET TO AFTER THE DIMENSION IS CHANGED
     * firstDigit is the first digit, secondDigit is the second digit. if we are working with a painting of width 32 and height 64
     * then we should input getIndexFromPaintingCoords(32, 64)
     **/
    private int getIndexFromPaintingCoords() {
        // LOOK WE ARE DIVIDING BY 16 TO MAKE THE NUMBERS 0, 1, 2, 3, and 4
        if (!isValidDimension()) {
            // todo: this number should be -1, make it so that it is
            return 0;
        }

        int serializedCord = encodeCords();
        int savedIndex = this.savedPaintingsInEachDimension[serializedCord].get();

        return savedIndex;
    }

    public void indexChanged() {
        createResult();
    }

    public List<PaintingVariant> getPossiblePaintings() {
        return this.possiblePaintings;
    }

    public void setPossiblePaintings(List<PaintingVariant> paintings) {
        this.possiblePaintings = paintings.stream().sorted(Comparator.comparing(ForgeRegistries.PAINTING_VARIANTS::getKey)).toList();
    }

    /**
     * Okay so the bases are 0, 1, 2, 3, 4 and the painting heights (as coded) are 0, 16, 32, 64, so yeah.
     */
    private void savePaintingForCurrentDimension() {
        if (!isValidDimension()) return;
        this.savedPaintingsInEachDimension[encodeCords()].set(getPaintingIndex());
    }

    public int getPaintingHeight() {
        return this.paintingHeight.get();
    }

    public int getPaintingWidth() {
        return this.paintingWidth.get();
    }

    public void setPaintingHeight(int newHeight) {
        this.paintingHeight.set(newHeight);
    }

    public void setPaintingWidth(int newWidth) {
        this.paintingWidth.set(newWidth);
    }

    public int getPossiblePaintingsSize() {
        return possiblePaintings.size();
    }

    public int getPaintingIndex() {
        return this.paintingIndex.get();
    }

    public void setPaintingIndex(int newIndex) {
        this.paintingIndex.set(newIndex);
    }

    public boolean isValidPaintingIndex(int index) {
        return index >= 0 && index < this.getPossiblePaintingsSize();
    }

    private boolean isValidDimension() {
        if (getPaintingHeight() <= MIN_DIMENSION || getPaintingHeight() > MAX_DIMENSION) return false;
        if (getPaintingWidth() <= MIN_DIMENSION || getPaintingWidth() > MAX_DIMENSION) return false;

        return true;
    }

    public PaintingVariant getCurrentPainting() {
        try {
            return getPossiblePaintings().get(getPaintingIndex());
        }
        catch (IndexOutOfBoundsException e) {
            return ForgeRegistries.PAINTING_VARIANTS.getValue(PaintingVariants.KEBAB.location());
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, EaselModBlocks.EASEL.get());
    }

    public void registerUpdateListener(Runnable listener) {
        this.slotUpdateListener = listener;
    }

    private int encodeCords() {
        return encodeTuple(getPaintingWidth() / 16, getPaintingHeight() / 16);
    }

    private static int encodeTuple(int width, int height) {
        return MathUtil.base4ExceptTheNumbersAre1234InsteadOf0123(width, height);
    }

    @Override
    public @NotNull MenuType<?> getType() {
        return EaselModMenuTypes.EASEL_MENU.get();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> {
            this.clearContainer(player, this.inputContainer);
        });
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
