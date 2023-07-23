package uz.dachatop.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.dachatop.service.SubscriptionService;



@Component
@Slf4j
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionService subscriptionService;

    //0 * * * *
    @Scheduled(cron = "0 *  * * *")
    public void scheduler() throws InterruptedException {
        subscriptionService.changeSubscriptionStatus();
    }
}
