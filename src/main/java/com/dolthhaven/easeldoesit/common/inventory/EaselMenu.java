package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.core.registry.EaselModMenuTypes;
import com.dolthhaven.easeldoesit.core.registry.EaselModSoundEvents;
import com.dolthhaven.easeldoesit.other.util.MathUtil;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EaselMenu extends AbstractContainerMenu {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/common/inventory/SawmillMenu.java
    private static final int MIN_DIMENSION = 16;
    private static final int MAX_DIMENSION = 64;

    private final ContainerLevelAccess access;
    long lastSoundTime;

    // slots
    Runnable slotUpdateListener = () -> {
    };
    public final Slot inputSlot;
    public final Slot resultSlot;
    public final Container inputContainer = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            EaselMenu.this.slotsChanged(this);
            EaselMenu.this.slotUpdateListener.run();
        }
    };
    final ResultContainer resultContainer = new ResultContainer();

    private final DataSlot paintingHeight = DataSlot.standalone();
    private final DataSlot paintingWidth = DataSlot.standalone();
    private final DataSlot paintingIndex = DataSlot.standalone();
    private final DataSlot[] savedIndexInEachDimension = new DataSlot[16]; // an array holding the last visited index before the painting dimension is changed
    private List<PaintingVariant> possiblePaintings = new ArrayList<>(); // a list of all paintings of (paintingHeight, paintingWidth)

    public EaselMenu(int id, Inventory inv) {
        this(id, inv, ContainerLevelAccess.NULL);
    }

    public EaselMenu(int id, Inventory inv, final ContainerLevelAccess access) {
        super(EaselModMenuTypes.EASEL_MENU.get(), id);
        this.access = access;

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
                ItemStack input = EaselMenu.this.inputSlot.remove(1);
                if (!input.isEmpty()) {
                    EaselMenu.this.createResult();
                }

                access.execute((level, pos) -> {
                    long l = level.getGameTime();
                    if (EaselMenu.this.lastSoundTime != l) {
                        level.playSound(null, pos, EaselModSoundEvents.UI_EASEL_TAKE_RESULT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        EaselMenu.this.lastSoundTime = l;
                    }
                });

                super.onTake(player, stack);
            }
        });
        // add slots on the thing

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // initialize all 16 data slots and stuff
        for (int i = 0; i < 16; i++) {
            DataSlot data = DataSlot.standalone();
            data.set(0);
            savedIndexInEachDimension[i] = (data);
        }

        // uhhh tracked data???
        // Set easel initial conditions
        short savedPaintingData = ((IDataManager) (inv.player)).getValue(EaselModTrackedData.PLAYER_CURRENT_PAINTING_INDEX);
        if (savedPaintingData == 0) {
            setPaintingWidth(0);
            setPaintingHeight(0);
            setPaintingIndex(0);
        }
        else {
            int[] dataInfo = EaselModTrackedData.decodePainting(savedPaintingData);
            setPaintingWidth(dataInfo[0]);
            setPaintingHeight(dataInfo[1]);
            setPaintingIndex(dataInfo[2]);
        }
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
            ItemStack stack = PaintingUtil.createPresetVariantPaintingStack(variant);
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
     * So uh yeah we kinda automated everytihng
     * When change dimensions you uh, look at the saved paintings in each dimension and do that index I suppose
     **/
    private int getIndexFromPaintingCoords() {
        if (!isValidDimension()) {
            // todo: this number should be -1, make it so that it is
            return 0;
        }

        int serializedCord = encodeCords();
        int savedIndex = this.savedIndexInEachDimension[serializedCord].get();

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
        this.savedIndexInEachDimension[encodeCords()].set(getPaintingIndex());
    }

    public int getPaintingHeight() {
        return this.paintingHeight.get();
    }

    public int getPaintingWidth() {
        return this.paintingWidth.get();
    }

    public void setPaintingHeight(int newHeight) {
        this.dimensionChangedPre();
        this.paintingHeight.set(newHeight);
        this.dimensionChangedPost();
    }

    public void setPaintingWidth(int newWidth) {
        this.dimensionChangedPre();
        this.paintingWidth.set(newWidth);
        this.dimensionChangedPost();
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
        if (getPaintingHeight() < MIN_DIMENSION || getPaintingHeight() > MAX_DIMENSION) return false;
        if (getPaintingWidth() < MIN_DIMENSION || getPaintingWidth() > MAX_DIMENSION) return false;

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
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack input = slot.getItem();
        Item item = input.getItem();
        ItemStack inputCopy = input.copy();
        // if is result slot
        if (index == this.inputSlot.getSlotIndex()) {
            item.onCraftedBy(input, player.level(), player);
            if (!this.moveItemStackTo(input, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            slot.onQuickCraft(input, inputCopy);
        }
        else if (index == this.resultSlot.getSlotIndex()) {  // if is input slot
            if (!this.moveItemStackTo(input, 2, 38, true)) {
                return ItemStack.EMPTY;
            }
        }
        else if (input.is(Items.PAINTING)) {
            if (!this.moveItemStackTo(input, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (index >= 2 && index < 29) {
            if (!this.moveItemStackTo(input, 29, 38, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (index >= 29 && index < 38 && !this.moveItemStackTo(input, 2, 29, false)) {
            return ItemStack.EMPTY;
        }

        if (input.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        }

        slot.setChanged();
        if (input.getCount() == inputCopy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, input);
        this.broadcastChanges();

        return inputCopy;
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

        savePainting(player);
    }

    private void savePainting(@NotNull Player player) {
        if (!isValidDimension()) return;

        IDataManager manager = (IDataManager) player;
        short paintingIndex = EaselModTrackedData.encodePainting(new int[]{
                this.getPaintingWidth() / 16 - 1,
                this.getPaintingHeight() / 16 - 1,
                this.getPaintingIndex()});

        manager.setValue(EaselModTrackedData.PLAYER_CURRENT_PAINTING_INDEX, paintingIndex);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int invRow = 0; invRow < 3; ++invRow) {
            for (int invCol = 0; invCol < 9; ++invCol) {
                this.addSlot(new Slot(playerInventory, invCol + invRow * 9 + 9, 8 + invCol * 18, 84 + invRow * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
}
