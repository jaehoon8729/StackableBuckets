package com.stackalbeBuckets.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Items.class)
public class ItemsMixin {
    // Items 클래스가 초기화될 때 실행
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyBucketStackSizes(CallbackInfo info) {
        try {
            // 필드에 직접 접근하여 maxCount 값을 변경
            setMaxCount(Items.BUCKET, 16);
            setMaxCount(Items.WATER_BUCKET, 16);
            setMaxCount(Items.LAVA_BUCKET, 16);
            setMaxCount(Items.MILK_BUCKET, 16);
            setMaxCount(Items.POWDER_SNOW_BUCKET, 16);

            // 물고기 양동이
            setMaxCount(Items.COD_BUCKET, 16);
            setMaxCount(Items.SALMON_BUCKET, 16);
            setMaxCount(Items.TROPICAL_FISH_BUCKET, 16);
            setMaxCount(Items.PUFFERFISH_BUCKET, 16);

            // 기타 양동이
            setMaxCount(Items.AXOLOTL_BUCKET, 16);
            setMaxCount(Items.TADPOLE_BUCKET, 16);
        } catch (Exception e) {
            System.err.println("스택형 양동이 모드: 아이템 스택 크기 설정 중 오류 발생");
            e.printStackTrace();
        }
    }

    // 아이템의 최대 스택 크기를 설정하는 헬퍼 메서드
    private static void setMaxCount(Item item, int maxCount) {
        try {
            // 리플렉션을 사용하여 maxCount 필드에 접근
            java.lang.reflect.Field field = Item.class.getDeclaredField("maxCount");
            field.setAccessible(true);
            field.set(item, maxCount);
        } catch (Exception e) {
            System.err.println("스택형 양동이 모드: " + item + "의 스택 크기 설정 중 오류 발생");
            e.printStackTrace();
        }
    }
}