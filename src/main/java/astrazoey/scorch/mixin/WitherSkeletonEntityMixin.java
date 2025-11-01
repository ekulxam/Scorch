package astrazoey.scorch.mixin;

import astrazoey.scorch.registry.ScorchCriteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
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
public abstract class WitherSkeletonEntityMixin extends AbstractSkeletonEntity {


    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onDeath(DamageSource source) {

        if (source.getSource() instanceof FireballEntity) {
            Item skull = Items.WITHER_SKELETON_SKULL;
            dropItem((ServerWorld) this.getEntityWorld(), skull);

            //Grant advancement
            if(source.getAttacker() instanceof PlayerEntity) {
                //Direct hit from a player
                ScorchCriteria.WITHER_FIREBALL.trigger((ServerPlayerEntity) source.getAttacker());
            } else {
                //not a direct hit from a player, award advancement to the nearest player
                PlayerEntity nearestPlayer = this.getEntityWorld().getClosestPlayer(this, -1);
                ScorchCriteria.WITHER_FIREBALL.trigger((ServerPlayerEntity) nearestPlayer);
            }
        }

        super.onDeath(source);

    }


}