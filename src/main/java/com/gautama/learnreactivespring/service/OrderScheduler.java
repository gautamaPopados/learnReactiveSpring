package com.gautama.learnreactivespring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "order.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class OrderScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "${cron.generate}")
    public void scheduleOrder() {
        log.info("Запуск задачи по генерации заказа");

        orderService.generate()
                .subscribe(
                        order -> log.info("Создан заказ: {}", order),
                        error -> log.error("Ошибка при генерации заказа", error),
                        () -> log.debug("Завершение генерации заказа")
                );
    }
}