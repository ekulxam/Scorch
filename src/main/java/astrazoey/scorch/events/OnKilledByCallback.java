package astrazoey.scorch.events;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface OnKilledByCallback {
    Event<OnKilledByCallback> EVENT = EventFactory.createArrayBacked(OnKilledByCallback.class,
            (listeners) -> (player, killedEntity) -> {
                for (OnKilledByCallback listener : listeners) {
                    listener.onKilledBy(player, killedEntity);
                }
            });

    void onKilledBy(PlayerEntity player, LivingEntity killedEntity);
}
