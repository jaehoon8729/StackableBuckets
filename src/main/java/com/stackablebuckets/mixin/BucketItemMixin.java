package com.stackablebuckets.mixin;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// 버킷 아이템 믹신
@Mixin(BucketItem.class)
public class BucketItemMixin {
    // 새로운 Settings 객체를 생성할 때 maxCount 인자를 변경
    @ModifyArg(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V"),
            index = 0
    )
    private static Item.Settings modifyBucketSettings(Item.Settings settings) {
        return settings.maxCount(16); // 스택 사이즈를 16으로 설정
    }
}