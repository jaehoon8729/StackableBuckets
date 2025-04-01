package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

/**
 * Minecraft의 Items 클래스가 완전히 초기화된 후 실행되는 믹스인으로
 * 우유 양동이와 가루눈 양동이의 최대 스택 수를 직접 수정합니다.
 */
@Mixin(value = Items.class, priority = 2000)
public class SpecialBucketFixMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void initSpecialBuckets(CallbackInfo ci) {
        StackableBuckets.LOGGER.info("특수 양동이 스택 크기 직접 수정 시도");

        try {
            // 리플렉션을 통해 maxCount 필드에 직접 접근 시도
            setFieldValueDirectly(Items.MILK_BUCKET, "maxCount", 16);
            setFieldValueDirectly(Items.POWDER_SNOW_BUCKET, "maxCount", 16);

            // 확인
            StackableBuckets.LOGGER.info("우유 양동이 수정 후 최대 스택: {}", Items.MILK_BUCKET.getMaxCount());
            StackableBuckets.LOGGER.info("가루눈 양동이 수정 후 최대 스택: {}", Items.POWDER_SNOW_BUCKET.getMaxCount());
        } catch (Exception e) {
            StackableBuckets.LOGGER.error("특수 양동이 직접 수정 실패: {}", e.getMessage());
        }
    }

    /**
     * 자바 리플렉션을 사용해 객체의 필드 값을 직접 수정합니다.
     * 상속 계층 구조를 모두 탐색하여 필드를 찾습니다.
     */
    private static void setFieldValueDirectly(Object obj, String fieldName, Object value) throws Exception {
        Class<?> clazz = obj.getClass();
        boolean fieldFound = false;

        // 상속 계층 구조를 따라 필드 탐색
        while (clazz != null && !fieldFound) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(obj, value);
                StackableBuckets.LOGGER.info("{} 클래스의 {} 필드 값을 {}로 수정 성공",
                        clazz.getName(), fieldName, value);
                fieldFound = true;
            } catch (NoSuchFieldException e) {
                // 상위 클래스로 이동
                clazz = clazz.getSuperclass();
            }
        }

        if (!fieldFound) {
            throw new NoSuchFieldException(fieldName + " 필드를 찾을 수 없습니다");
        }
    }
}