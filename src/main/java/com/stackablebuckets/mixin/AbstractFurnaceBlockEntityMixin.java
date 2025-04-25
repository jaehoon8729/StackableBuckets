package com.stackablebuckets.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 화로에서 용암 양동이가 연료로 사용될 때 빈 양동이를 생성하는 Mixin
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    
    /**
     * 연료 소비 시 빈 양동이 처리를 위한 메서드
     * 연료 슬롯의 아이템이 용암 양동이인 경우, 화로 앞으로 빈 양동이를 생성합니다.
     */
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
        )
    )
    private static void onFuelConsume(ServerWorld world, BlockPos pos, BlockState state, 
                                    AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        // 연료 슬롯(인덱스 1)의 아이템 스택 가져오기
        ItemStack fuelStack = blockEntity.getStack(1);
        
        // 용암 양동이인 경우, 빈 양동이 생성
        if (fuelStack.getItem() == Items.LAVA_BUCKET && fuelStack.getCount() > 1) {
            // 빈 양동이 생성 및 던지기
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            Direction direction = state.get(net.minecraft.block.AbstractFurnaceBlock.FACING);
            double x = pos.getX() + 0.5 + direction.getOffsetX() * 0.7;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5 + direction.getOffsetZ() * 0.7;
            
            ItemEntity itemEntity = new ItemEntity(world, x, y, z, emptyBucket);
            itemEntity.setVelocity(
                direction.getOffsetX() * 0.1,
                0.1,
                direction.getOffsetZ() * 0.1
            );
            world.spawnEntity(itemEntity);
        }
    }
}
