package astrazoey.scorch.events;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public interface FinishUsingCallback {
    Event<FinishUsingCallback> EVENT = EventFactory.createArrayBacked(FinishUsingCallback.class,
            (listeners) -> (player, itemStack) -> {
                for (FinishUsingCallback listener : listeners) {
                    ActionResult result = listener.finishUsing(player, itemStack);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult finishUsing(PlayerEntity player, ItemStack itemStack);
}
