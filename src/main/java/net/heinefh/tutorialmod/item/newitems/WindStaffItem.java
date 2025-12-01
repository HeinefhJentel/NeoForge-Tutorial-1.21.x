package net.heinefh.tutorialmod.item.newitems;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;


public class WindStaffItem extends Item {

    public static final int COOLDOWN_TICKS = 20;
    public static final float BASE_PROJECTILE_SPEED = 2.4F;

    public WindStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW; // trident-style charging animation
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000; // same as bow/trident
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand); // start charging
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {

        if (!(living instanceof Player player)) return;

        int chargeTime = this.getUseDuration(stack, living) - timeLeft;
        if (chargeTime < 10) return;

        if (!level.isClientSide()) {

            WindCharge charge = new WindCharge(EntityType.WIND_CHARGE, level); // correct constructor
            charge.setOwner(player);
            charge.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            charge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, BASE_PROJECTILE_SPEED, 1.0F);
            level.addFreshEntity(charge);

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }
    }

}
