package com.stackablebuckets;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackableBuckets implements ModInitializer {
    public static final String MOD_ID = "stackablebuckets";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("스택형 양동이 모드가 초기화되었습니다!");

        // 양동이 아이템 스택 크기 로깅
        logBucketInfo("물 양동이", Items.WATER_BUCKET);
        logBucketInfo("용암 양동이", Items.LAVA_BUCKET);
        logBucketInfo("우유 양동이", Items.MILK_BUCKET);
        logBucketInfo("가루눈 양동이", Items.POWDER_SNOW_BUCKET);
        logBucketInfo("빈 양동이", Items.BUCKET);
        logBucketInfo("연어 양동이", Items.SALMON_BUCKET);

        // ID로 특정 아이템을 찾아서 로깅
        LOGGER.info("등록된 양동이 아이템 확인:");
        Registries.ITEM.forEach(item -> {
            String itemPath = Registries.ITEM.getId(item).getPath();
            if (itemPath.contains("bucket")) {
                LOGGER.info("양동이 아이템 찾음: {} (최대 스택: {})",
                        itemPath, item.getMaxCount());

                // 특히 우유와 가루눈 양동이 정보 상세 출력
                if (itemPath.equals("milk_bucket") || itemPath.equals("powder_snow_bucket")) {
                    LOGGER.info("  - 클래스: {}", item.getClass().getName());
                    LOGGER.info("  - 부모 클래스: {}", item.getClass().getSuperclass().getName());
                    LOGGER.info("  - getMaxCount 메서드 출력: {}", item.getMaxCount());
                }
            }
        });
    }

    private void logBucketInfo(String name, net.minecraft.item.Item item) {
        LOGGER.info("{} 정보 - ID: {}, 최대 스택: {}",
                name,
                Registries.ITEM.getId(item),
                item.getMaxCount());
    }
}