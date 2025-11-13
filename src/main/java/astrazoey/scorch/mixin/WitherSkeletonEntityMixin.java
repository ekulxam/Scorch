package astrazoey.scorch.mixin;

import astrazoey.scorch.registry.ScorchCriteria;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends LivingEntityMixin {

    public WitherSkeletonEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void wrapOnDeath(DamageSource source, Operation<Void> original) {
        if (source.getSource() instanceof FireballEntity && this.getEntityWorld() instanceof ServerWorld serverWorld) {
            Item skull = Items.WITHER_SKELETON_SKULL;
            dropItem(serverWorld, skull);

            //Grant advancement
            if (source.getAttacker() instanceof ServerPlayerEntity serverPlayer) {
                //Direct hit from a player
                ScorchCriteria.WITHER_FIREBALL.trigger(serverPlayer);
            } else {
                //not a direct hit from a player, award advancement to the nearest player
                PlayerEntity nearestPlayer = serverWorld.getClosestPlayer(this, -1);
                if (nearestPlayer instanceof ServerPlayerEntity serverPlayer) {
                    ScorchCriteria.WITHER_FIREBALL.trigger(serverPlayer);
                }
            }
        }
        super.wrapOnDeath(source, original);
    }
}