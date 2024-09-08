package com.dolthhaven.easeldoesit.common.inventory;

import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.core.registry.EaselModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EaselMenu extends AbstractContainerMenu {
    // https://github.com/team-abnormals/woodworks/blob/1.20.x/src/main/java/com/teamabnormals/woodworks/common/inventory/SawmillMenu.java

    private final ContainerLevelAccess access;

    // slots
    Runnable slotUpdateListener = () -> {
    };
    private ItemStack input = ItemStack.EMPTY;
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
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        ItemStack itemstack = this.inputSlot.getItem();
        super.slotsChanged(container);
        if (container == this.inputContainer) {
            createResult();
        }
    }


    void createResult() {
        if (this.inputSlot.getItem().is(Items.PAINTING)) {
            ItemStack stack = new ItemStack(Items.DIAMOND);
            this.resultSlot.set(stack);
        }
        else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
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
