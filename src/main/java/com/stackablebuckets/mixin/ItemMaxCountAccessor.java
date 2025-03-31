package com.stackablebuckets.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.item.Item;
import net.minecraft.component.ComponentMap;

/**
 * 아이템의 내부 컴포넌트에 접근하기 위한 액세서 인터페이스
 */
@Mixin(Item.class)
public interface ItemMaxCountAccessor {

    /**
     * 아이템의 컴포넌트 맵에 접근
     */
    @Accessor("components")
    ComponentMap getComponents();
}