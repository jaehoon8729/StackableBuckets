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
                // ComponentMap의 내부 맵에 접근 시도
                Field mapField = ComponentMap.class.getDeclaredField("map");
                mapField.setAccessible(true);
                Object map = mapField.get(this.components);

                // 맵에 MAX_STACK_SIZE 값을 설정
                Method putMethod = map.getClass().getMethod("put", Object.class, Object.class);
                putMethod.invoke(map, DataComponentTypes.MAX_STACK_SIZE, 16);

                StackableBuckets.LOGGER.info("특수 양동이 컴포넌트 수정 성공: {}", itemPath);
            } catch (Exception e) {
                StackableBuckets.LOGGER.error("특수 양동이 컴포넌트 수정 실패: {}", e.getMessage());
            }
        }
    }
}