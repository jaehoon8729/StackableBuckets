package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public class RegistryMixin {

    @Inject(method = "register(Lnet/minecraft/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("RETURN"))
    private static <V> void onRegister(Registry<V> registry, Identifier id, V entry, CallbackInfoReturnable<V> info) {
        if (registry == Registries.ITEM && entry instanceof Item) {
            Item item = (Item)entry;
            String itemPath = id.getPath();

            if (itemPath.equals("milk_bucket") || itemPath.equals("powder_snow_bucket")) {
                StackableBuckets.LOGGER.info("특수 양동이 등록 감지: {}", itemPath);

                try {
                    // Item.Settings 수정 시도
                    java.lang.reflect.Field settingsField = Item.class.getDeclaredField("settings");
                    settingsField.setAccessible(true);
                    Object settings = settingsField.get(item);

                    // maxCount 수정 시도
                    java.lang.reflect.Method maxCountMethod = settings.getClass().getMethod("maxCount", int.class);
                    maxCountMethod.invoke(settings, 16);

                    StackableBuckets.LOGGER.info("특수 양동이 스택 크기 수정 성공: {}", itemPath);
                } catch (Exception e) {
                    StackableBuckets.LOGGER.error("특수 양동이 스택 크기 수정 실패: {}", e.getMessage());
                }
            }
        }
    }
}