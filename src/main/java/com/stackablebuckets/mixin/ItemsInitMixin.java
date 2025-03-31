package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Items.class)
public class ItemsInitMixin {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onItemsInitialized(CallbackInfo ci) {
        StackableBuckets.LOGGER.info("Items 클래스 초기화 완료, 우유 양동이와 가루눈 양동이 정보 확인");

        // 아이템 정보 출력 (디버깅 목적)
        try {
            int milkBucketMaxCount = Items.MILK_BUCKET.getMaxCount();
            int powderSnowBucketMaxCount = Items.POWDER_SNOW_BUCKET.getMaxCount();

            StackableBuckets.LOGGER.info("우유 양동이 초기 최대 스택: {}, 클래스: {}",
                    milkBucketMaxCount,
                    Items.MILK_BUCKET.getClass().getName());

            StackableBuckets.LOGGER.info("가루눈 양동이 초기 최대 스택: {}, 클래스: {}",
                    powderSnowBucketMaxCount,
                    Items.POWDER_SNOW_BUCKET.getClass().getName());
        } catch (Exception e) {
            StackableBuckets.LOGGER.error("특수 양동이 정보 확인 중 오류: {}", e.getMessage());
        }
    }
}