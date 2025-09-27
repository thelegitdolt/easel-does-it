package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.core.registry.EaselModMenuTypes;
import com.dolthhaven.easeldoesit.core.registry.EaselModSoundEvents;
import com.dolthhaven.easeldoesit.other.util.MathUtil;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class EaselMenu extends AbstractContainerMenu {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/common/inventory/SawmillMenu.java
    private static final int MIN_DIMENSION = 1;
    private static final int MAX_DIMENSION = 4;
    private static final List<PaintingVariant> EMPTY = List.of();

    private final ContainerLevelAccess access;
    private final Level level;

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
    private final List<List<PaintingVariant>> possiblePaintings = new ArrayList<>(16); // a list of all paintings of (paintingHeight, paintingWidth)

    public EaselMenu(int id, Inventory inv) {
        this(id, inv, ContainerLevelAccess.NULL);
    }

    public EaselMenu(int id, Inventory inv, final ContainerLevelAccess access) {
        super(EaselModMenuTypes.EASEL_MENU.get(), id);
        this.level = inv.player.level();
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
            addDataSlot(data);
            savedIndexInEachDimension[i] = (data);
        }

        addDataSlot(paintingHeight);
        addDataSlot(paintingWidth);
        addDataSlot(paintingIndex);

        // uhhh tracked data???
        // Set easel initial conditions
        syncPlayerData(inv.player);
        initPaintings(level.registryAccess());
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        super.slotsChanged(container);

        if (container == this.inputContainer) {
            createResult();
        }
    }

    private void createResult() {
        if (this.inputSlot.getItem().is(Items.PAINTING) && this.getCurrentPainting().isEmpty()) {
            this.getCurrentPainting().ifPresent(variant -> {
                ItemStack stack = PaintingUtil.makeStack(variant, level.registryAccess());
                this.resultSlot.set(stack);
            });
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    public void dimensionChangedPre() {
        savePaintingForCurrentDimension();
    }

    public void dimensionChangedPost() {
        ItemStack inputStack = this.inputSlot.getItem();
        setPaintingIndex(savedIndex());

        if (inputStack.is(Items.PAINTING)) {
            createResult();
        }
    }

    private int savedIndex() {
        if (!isLegalDimensions()) {
            return -1;
        }

        int serializedCord = encodeCords();
        return this.savedIndexInEachDimension[serializedCord].get();
    }

    public void indexChanged() {
        createResult();
    }

    public List<PaintingVariant> getPaintings() {
        return isLegalDimensions() ? possiblePaintings.get(encodeCords()) : EMPTY;
    }

    private void savePaintingForCurrentDimension() {
        if (!isLegalDimensions()) return;
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
        return getPaintings().size();
    }

    public int getPaintingIndex() {
        return this.paintingIndex.get();
    }

    public void setPaintingIndex(int newIndex) {
        this.paintingIndex.set(newIndex);
        indexChanged();
    }

    public boolean isLegalIndex() {
        return isLegalIndex(getPaintingIndex());
    }

    public boolean isLegalIndex(int index) {
        return index >= 0 && index < this.getPossiblePaintingsSize();
    }

    public boolean isLegalDimensions() {
        if (getPaintingHeight() < MIN_DIMENSION || getPaintingHeight() > MAX_DIMENSION) return false;
        if (getPaintingWidth() < MIN_DIMENSION || getPaintingWidth() > MAX_DIMENSION) return false;
        return true;
    }

    public Optional<PaintingVariant> getCurrentPainting() {
        PaintingVariant variant = !isLegalIndex() ? null : getPaintings().get(getPaintingIndex());
        return Optional.ofNullable(variant);
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
        return encodeCords(getPaintingWidth(), getPaintingHeight());
    }

    private int encodeCords(int width, int height) {
        if (width == 0 || height == 0) return -1;
        return MathUtil.base4Minus5(width, height);
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
        if (!isLegalDimensions()) return;

        IDataManager manager = (IDataManager) player;
        short paintingIndex = EaselModTrackedData.encodePainting(new int[]{
                this.getPaintingWidth() - 1,
                this.getPaintingHeight() - 1,
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

    private void syncPlayerData(Player player) {
        short savedPaintingData = ((IDataManager) (player)).getValue(EaselModTrackedData.PLAYER_CURRENT_PAINTING_INDEX);
        if (savedPaintingData == 0) {
            setPaintingWidth(0);
            setPaintingHeight(0);
            setPaintingIndex(0);
        }
        else {
            Vec3i dataInfo = EaselModTrackedData.decodePainting(savedPaintingData);
            setPaintingWidth(dataInfo.getX());
            setPaintingHeight(dataInfo.getY());
            setPaintingIndex(dataInfo.getZ());
        }
    }

    private void initPaintings(RegistryAccess access) {
        for (int i = 0; i < 16; i++) {
            this.possiblePaintings.add(new ArrayList<>());
        }

        PaintingUtil.tagged(PaintingVariantTags.PLACEABLE, access, painting -> painting.width() <= 64 && painting.height() <= 64)
                .forEach((painting -> {
                    int entry = encodeCords(painting.width(), painting.height());
                    possiblePaintings.get(entry).add(painting);
                }));

        possiblePaintings.forEach(list -> list.sort(Comparator.comparing(PaintingVariant::assetId)));
    }
}
