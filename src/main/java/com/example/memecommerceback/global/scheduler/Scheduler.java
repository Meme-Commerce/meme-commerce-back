package com.example.memecommerceback.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final DailyScheduler dailyScheduler;
    private final WeeklyScheduler weeklyScheduler;
    private final MonthlyScheduler monthlyScheduler;
    private final YearlyScheduler yearlyScheduler;

    @Scheduled(cron = "0 0 10,11,12,19,20,21 * * *")
    public void executeAllowedTimeTask() {
        // 10, 11, 12, 19, 20, 21시 정각에만 실행되는 로직
        dailyScheduler.execute(DailyJobType.PRODUCT_OPEN);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void executeClosedTimeTask() {
        // 10, 11, 12, 19, 20, 21시 정각에만 실행되는 로직
        dailyScheduler.execute(DailyJobType.PRODUCT_CLOSE);
    }

    // TODO : 정산 시스템 구축 시, 15시에 처리
    //  이유 : 정산 처리 예정인 시스템을 해당 시간에 처리
    //  관리자는 처리 예정인 정산 테이블을 보고 간편하게 처리 가능하게 만들기 위해
    @Scheduled(cron = "0 0 15 * * *")
    public void executeDailyTask() {
        dailyScheduler.execute(DailyJobType.SETTLEMENT);
    }

    @Scheduled(cron = "0 0 15 * * SUN")
    public void executeWeeklyTask() {
        weeklyScheduler.execute();
    }

    @Scheduled(cron = "0 0 15 1 * *")
    public void executeMonthlyTask() {
        monthlyScheduler.execute();
    }

    // 매년 1월 1일 00:00 실행
    @Scheduled(cron = "0 0 15 1 1 *")
    public void executeYearlyTask() {
        yearlyScheduler.execute();
    }
}