package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 아이템 초기화 시 특수 양동이의 컴포넌트 맵을 수정
 */
@Mixin(Item.class)
public class ComponentItemMixin {

    @Shadow @Final private ComponentMap components;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onItemInitialized(Item.Settings settings, CallbackInfo ci) {
        Item self = (Item)(Object)this;
        String itemPath = Registries.ITEM.getId(self).getPath();

        if (itemPath.equals("milk_bucket") || itemPath.equals("powder_snow_bucket")) {
            StackableBuckets.LOGGER.info("특수 양동이 초기화 감지: {}", itemPath);

            try {
                // ComponentMap의 내부 맵에 접근하는 다른 방법 시도
                Field mapField = ComponentMap.class.getDeclaredField("map");
                mapField.setAccessible(true);
                Object mapObject = mapField.get(this.components);

                if (mapObject instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>)mapObject;

                    // 현재 값을 로그로 출력
                    StackableBuckets.LOGGER.info("컴포넌트 맵 정보: {}", map);
                    StackableBuckets.LOGGER.info("현재 MAX_STACK_SIZE 값: {}", map.get(DataComponentTypes.MAX_STACK_SIZE));

                    // 리플렉션을 통해 맵에 값 설정 시도
                    Method putMethod = map.getClass().getMethod("put", Object.class, Object.class);
                    putMethod.setAccessible(true);
                    putMethod.invoke(map, DataComponentTypes.MAX_STACK_SIZE, 16);

                    StackableBuckets.LOGGER.info("특수 양동이 컴포넌트 수정 성공: {}", itemPath);
                    StackableBuckets.LOGGER.info("수정 후 MAX_STACK_SIZE 값: {}", map.get(DataComponentTypes.MAX_STACK_SIZE));
                } else {
                    StackableBuckets.LOGGER.error("맵 객체 타입이 예상과 다름: {}", mapObject.getClass().getName());
                }
            } catch (Exception e) {
                StackableBuckets.LOGGER.error("특수 양동이 컴포넌트 수정 실패: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}