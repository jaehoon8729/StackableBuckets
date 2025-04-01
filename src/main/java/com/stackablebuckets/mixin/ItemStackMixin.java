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

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    // 로그를 한 번만 출력하기 위한 세트
    private static final Set<String> LOGGED_ITEMS = new HashSet<>();

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void modifyMaxCount(CallbackInfoReturnable<Integer> info) {
        Item item = this.getItem();
        Identifier itemId = Registries.ITEM.getId(item);
        String itemPath = itemId.getPath();

        // 모든 양동이 아이템 체크 (특수 양동이 포함)
        boolean isBucket = itemPath.contains("bucket");

        if (isBucket) {
            // 로그를 한 번만 출력
            if (!LOGGED_ITEMS.contains(itemPath)) {
                StackableBuckets.LOGGER.info("스택형 양동이 모드(ItemStack): {} 아이템 스택 크기를 16으로 설정", itemPath);
                LOGGED_ITEMS.add(itemPath);
            }

            // 스택 크기를 16으로 설정
            info.setReturnValue(16);
        }
    }
}