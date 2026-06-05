package com.andeva.atelier.platform.core.application.internal.commandservices;

import com.andeva.atelier.platform.core.application.commandservices.SubscriptionCommandService;
import com.andeva.atelier.platform.core.domain.model.commands.AssignSubscriptionCommand;
import com.andeva.atelier.platform.core.domain.model.commands.CancelSubscriptionCommand;
import com.andeva.atelier.platform.core.domain.model.entities.BranchSubscription;
import com.andeva.atelier.platform.core.domain.repositories.BranchRepository;
import com.andeva.atelier.platform.core.domain.repositories.BranchSubscriptionRepository;
import com.andeva.atelier.platform.core.domain.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final BranchSubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final BranchRepository branchRepository;

    public SubscriptionCommandServiceImpl(
            BranchSubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository planRepository,
            BranchRepository branchRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public Optional<BranchSubscription> handle(AssignSubscriptionCommand command) {
        if (!branchRepository.existsById(command.branchId())) {
            throw new IllegalArgumentException("Branch does not exist.");
        }

        var plan = planRepository.findById(command.planId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription plan does not exist."));

        // Todo: Validate limits when downgrading using other bounded context queries if necessary

        var existingSubscription = subscriptionRepository.findActiveByBranchId(command.branchId());
        existingSubscription.ifPresent(sub -> {
            sub.cancel(new Date());
            subscriptionRepository.save(sub);
        });

        Date startDate = new Date();
        LocalDate localEndDate = LocalDate.now().plusMonths(command.billingCycle().name().equals("MONTHLY") ? 1 : 12);
        Date endDate = Date.from(localEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        var newSubscription = new BranchSubscription(
                command.branchId(),
                command.planId(),
                command.billingCycle(),
                startDate,
                endDate
        );

        subscriptionRepository.save(newSubscription);
        return Optional.of(newSubscription);
    }

    @Override
    public Optional<BranchSubscription> handle(CancelSubscriptionCommand command) {
        var existingSubscription = subscriptionRepository.findActiveByBranchId(command.branchId());
        if (existingSubscription.isEmpty()) {
            throw new IllegalArgumentException("Branch does not have an active subscription.");
        }

        var sub = existingSubscription.get();
        // Just mark as CANCELED but it remains valid until endDate based on domain rules
        sub.cancel(new Date());
        subscriptionRepository.save(sub);

        return Optional.of(sub);
    }
}
