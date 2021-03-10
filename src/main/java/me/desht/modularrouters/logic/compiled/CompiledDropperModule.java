package me.desht.modularrouters.logic.compiled;

import me.desht.modularrouters.block.tile.TileEntityItemRouter;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.item.augment.PickupDelayAugment;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class CompiledDropperModule extends CompiledModule {
    private final int pickupDelay;  // ticks

    public CompiledDropperModule(TileEntityItemRouter router, ItemStack stack) {
        super(router, stack);

        pickupDelay = getAugmentCount(ModItems.PICKUP_DELAY_AUGMENT.get()) * PickupDelayAugment.TICKS_PER_AUGMENT;
    }

    @Override
    public boolean execute(@Nonnull TileEntityItemRouter router) {
        ItemStack stack = router.getBufferItemStack();
        if (getFilter().test(stack) && isRegulationOK(router, false)) {
            int nItems = Math.min(getItemsPerTick(router), stack.getCount() - getRegulationAmount());
            if (nItems <= 0) {
                return false;
            }
            ItemStack toDrop = router.peekBuffer(nItems);
            BlockPos pos = getTarget().gPos.pos();
            Direction face = getTarget().face;
            ItemEntity item = new ItemEntity(router.getLevel(),
                    pos.getX() + 0.5 + 0.2 * face.getStepX(),
                    pos.getY() + 0.5 + 0.2 * face.getStepY(),
                    pos.getZ() + 0.5 + 0.2 * face.getStepZ(),
                    toDrop);
            item.setPickUpDelay(pickupDelay);
            setupItemVelocity(router, item);
            router.getLevel().addFreshEntity(item);
            router.extractBuffer(toDrop.getCount());
            return true;
        } else {
            return false;
        }
    }

    void setupItemVelocity(TileEntityItemRouter router, ItemEntity item) {
        item.setDeltaMovement(0, 0, 0);
    }
}
