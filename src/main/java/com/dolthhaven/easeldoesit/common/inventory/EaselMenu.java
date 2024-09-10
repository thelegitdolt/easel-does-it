package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.core.registry.EaselModMenuTypes;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.client.Minecraft;
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

        // menu defaults to 0 when first opened
        setPaintingWidth(0);
        setPaintingHeight(0);
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        ItemStack inputStack = this.inputSlot.getItem();
        super.slotsChanged(container);
        // lol
        setPaintingIndex(isValidPaintingIndex(inputStack.getCount()) ? inputStack.getCount() : 0);

        if (container == this.inputContainer) {
            createResult();
        }
    }

    private void createResult() {
        EaselDoesIt.log("The painting index is: " + getPaintingIndex());
        if (this.inputSlot.getItem().is(Items.PAINTING) && isValidPaintingIndex(getPaintingIndex())) {
            PaintingVariant variant = getCurrentPainting();
            EaselDoesIt.log("SOMETHING IS SEXING.... REAL......");
            ItemStack stack = PaintingUtil.getStackFromPainting(variant);
            this.resultSlot.set(stack);
        }
        else {
            EaselDoesIt.log("NOT SEXING!!!!!");
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    /**
     * Method ran when you change the paintings
     * It will:
     * - Reset possible paintings
     * - Reset index to 0
     */
    public void dimensionChanged() {
        ItemStack inputStack = this.inputSlot.getItem();
        setPossiblePaintings(PaintingUtil.getAllPaintingsOfDimensions(getPaintingWidth(), getPaintingHeight()));

        if (inputStack.is(Items.PAINTING)) {
            createResult();
        }
    }

    public List<PaintingVariant> getPossiblePaintings() {
        return this.possiblePaintings;
    }

    public void setPossiblePaintings(List<PaintingVariant> paintings) {
        this.possiblePaintings = paintings.stream().sorted(Comparator.comparing(ForgeRegistries.PAINTING_VARIANTS::getKey)).toList();
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

    private boolean isValidPaintingIndex(int index) {
        return index >= 0 && index < this.possiblePaintings.size();
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
