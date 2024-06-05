package com.dolthhaven.easeldoesit.common.gui.slots;

import com.dolthhaven.easeldoesit.common.gui.EaselMenu;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EaselOutputSlot extends Slot {
    private final EaselMenu easelMenu;
    private final ContainerLevelAccess access;

    public EaselOutputSlot(Container container, ContainerLevelAccess access, EaselMenu easelMenu, int id, int xOffset, int yOffset) {
        super(container, id, xOffset, yOffset);
        this.easelMenu = easelMenu;
        this.access = access;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        stack.onCraftedBy(player.level(), player, stack.getCount());
        this.easelMenu.getResultContainer().awardUsedRecipes(player, this.getRelevantItems());

        ItemStack outputStack = this.easelMenu.getInputSlot().remove(1);

        if (!outputStack.isEmpty()) {
            this.easelMenu.setupResultSlot();
        }

        access.execute((level, p_40365_) -> {
            long gameTime = level.getGameTime();
            if (this.easelMenu.getLastSoundTime() != gameTime) {
                level.playSound(null, p_40365_, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.easelMenu.setLastSoundTime(gameTime);
            }
        });

        super.onTake(player, stack);
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.easelMenu.getInputSlot().getItem());
    }
}
