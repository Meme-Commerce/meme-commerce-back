package com.example.memecommerceback.global.scheduler;

import com.example.memecommerceback.domain.products.service.ProductServiceV1;
import com.example.memecommerceback.global.utils.DateUtils;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyScheduler implements SchedulerTask {

    private final ProductServiceV1 productService;

    @Override
    public void execute() {

    }

    // 상품 상태 변환 로직
    public void execute(DailyJobType dailyJobType) {
        switch (dailyJobType) {
            // 10, 11, 12, 19, 20, 21시에만 할 작업
            case PRODUCT_OPEN:
                productService.updateOnSaleStatus();
                break;
            // 자정에 Product의 상태를
            case PRODUCT_CLOSE:
                productService.updateHiddenStatus();
                break;
            // 15시에만 할 작업 (정산)
            case SETTLEMENT:
                break;
        }
    }
}
