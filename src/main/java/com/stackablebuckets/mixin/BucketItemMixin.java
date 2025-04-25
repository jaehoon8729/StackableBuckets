package com.stackablebuckets.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {
    
    @Inject(method = "use", at = @At("RETURN"))
    private void afterUse(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient) return;
        
        ItemStack handStack = player.getStackInHand(hand);
        ItemStack returnedStack = cir.getReturnValue();
        
        // 양동이가 사용되었고(빈 양동이로 바뀜), 스택이 있는 경우
        if (!handStack.isEmpty() && returnedStack.getItem() instanceof BucketItem && 
            handStack.getCount() > 1 && handStack.getItem() != returnedStack.getItem()) {
            
            // 원래 스택에서 하나 감소 (이미 하나는 사용됨)
            handStack.decrement(1);
            
            // 빈 양동이를 플레이어 앞으로 던지기
            player.dropItem(returnedStack, false);
            
            // 원래 아이템 스택을 반환 (스택을 그대로 유지)
            cir.setReturnValue(handStack);
        }
    }
}
