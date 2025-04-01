package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 우유 양동이와 가루눈 양동이의 getMaxCount 메서드를 높은 우선순위로 오버라이드
 */
@Mixin(value = Item.class, priority = 1000)
public class SpecialBucketsMixin {

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void modifyMaxCount(CallbackInfoReturnable<Integer> info) {
        Item self = (Item)(Object)this;
        String itemPath = Registries.ITEM.getId(self).getPath();

        // 특정 아이템인 경우에만 처리
        if (itemPath.equals("milk_bucket") || itemPath.equals("powder_snow_bucket")) {
            StackableBuckets.LOGGER.info("특수 양동이 최대 스택 크기 오버라이드 (우선순위 1000): {}", itemPath);
            info.setReturnValue(16);
            info.cancel();
        }
    }
}