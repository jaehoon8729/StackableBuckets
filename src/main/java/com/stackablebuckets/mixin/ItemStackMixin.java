package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = ItemStack.class, priority = 1500)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    // 로그를 한 번만 출력하기 위한 세트
    private static final Set<String> LOGGED_ITEMS = new HashSet<>();

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void modifyMaxCount(CallbackInfoReturnable<Integer> info) {
        Item item = this.getItem();
        Identifier itemId = Registries.ITEM.getId(item);
        String itemPath = itemId.getPath();
        String fullId = itemId.toString();

        // 구체적인 ID 체크를 통해 특수 양동이 확인
        boolean isSpecialBucket = fullId.equals("minecraft:milk_bucket") || fullId.equals("minecraft:powder_snow_bucket");
        boolean isBucket = itemPath.contains("bucket");

        if (isSpecialBucket) {
            // 우선적으로 처리하기 위해 특수 양동이를 먼저 확인
            if (!LOGGED_ITEMS.contains(fullId)) {
                StackableBuckets.LOGGER.info("특수 양동이 스택 처리(ItemStack, 우선순위 1500): {}", fullId);
                LOGGED_ITEMS.add(fullId);
            }
            info.setReturnValue(16);
        } else if (isBucket) {
            // 일반 양동이 처리
            if (!LOGGED_ITEMS.contains(itemPath)) {
                StackableBuckets.LOGGER.info("스택형 양동이 모드(ItemStack): {} 아이템 스택 크기를 16으로 설정", itemPath);
                LOGGED_ITEMS.add(itemPath);
            }
            info.setReturnValue(16);
        }
    }
}