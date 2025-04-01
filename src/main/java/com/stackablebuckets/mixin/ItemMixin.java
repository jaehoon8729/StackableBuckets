package com.stackablebuckets.mixin;

import com.stackablebuckets.StackableBuckets;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(Item.class)
public class ItemMixin {

    // 로그를 한 번만 출력하기 위한 세트
    private static final Set<String> LOGGED_ITEMS = new HashSet<>();

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void modifyMaxCount(CallbackInfoReturnable<Integer> info) {
        Item self = (Item)(Object)this;
        Identifier itemId = Registries.ITEM.getId(self);
        String itemPath = itemId.getPath();

        // 모든 양동이 아이템 체크 (특수 양동이 포함)
        boolean isBucket = itemPath.contains("bucket");

        if (isBucket) {
            // 로그를 한 번만 출력
            if (!LOGGED_ITEMS.contains(itemPath)) {
                StackableBuckets.LOGGER.info("스택형 양동이 모드(Item): {} 아이템 스택 크기를 16으로 설정", itemPath);
                LOGGED_ITEMS.add(itemPath);
            }

            // 스택 크기를 16으로 설정
            info.setReturnValue(16);
        }
    }
}