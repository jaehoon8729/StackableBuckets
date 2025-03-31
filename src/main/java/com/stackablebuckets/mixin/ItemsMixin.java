package com.stackablebuckets.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ItemsMixin - 이미 생성된 아이템의 스택 크기를 변경하는 믹신
 */
@Mixin(Items.class)
public class ItemsMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyBucketStackSizes(CallbackInfo info) {
        // 리플렉션 대신 BucketItemMixin을 통해 이미 처리되므로 여기서는 로그만 출력
        System.out.println("스택형 양동이 모드: Items 클래스 초기화 후 처리 완료");
        System.out.println("스택형 양동이 모드: 모든 양동이 아이템의 스택 크기가 16으로 설정됩니다.");
    }
}