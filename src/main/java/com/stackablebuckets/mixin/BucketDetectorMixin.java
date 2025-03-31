package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 모든 getMaxCount 호출을 감시하여 로그로 출력
 */
@Mixin(Item.class)
public class BucketDetectorMixin {

    private static boolean logged = false;

    @Inject(method = "getMaxCount", at = @At("RETURN"))
    private void logMaxCount(CallbackInfoReturnable<Integer> info) {
        // 한 번만 로그 출력
        if (!logged) {
            Item self = (Item)(Object)this;
            String itemId = Registries.ITEM.getId(self).toString();

            if (itemId.equals("minecraft:milk_bucket") || itemId.equals("minecraft:powder_snow_bucket")) {
                StackableBuckets.LOGGER.info("getMaxCount가 호출됨: {}, 반환값: {}", itemId, info.getReturnValue());
                // 스택 트레이스 출력
                new Exception("스택 트레이스").printStackTrace();
                logged = true;
            }
        }
    }
}