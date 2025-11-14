package astrazoey.scorch.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface FinishUsingCallback {
    Event<FinishUsingCallback> EVENT = EventFactory.createArrayBacked(FinishUsingCallback.class,
            (listeners) -> (player, itemStack) -> {
                for (FinishUsingCallback listener : listeners) {
                    listener.finishUsing(player, itemStack);
                }
            });

    void finishUsing(PlayerEntity player, ItemStack itemStack);
}
