package astrazoey.scorch.criterion;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class GenericPlayerCriterion extends AbstractCriterion<GenericPlayerCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static final Codec<GenericPlayerCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(Conditions::new, Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return this.playerPredicate;
        }
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, conditions -> true);
    }
}
