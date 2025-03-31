package com.stackalbeBuckets;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackableBuckets implements ModInitializer {
    public static final String MOD_ID = "stackablebuckets";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("스택형 양동이 모드가 초기화되었습니다!");
        // 실제 작업은 믹신에서 이루어집니다
    }
}
