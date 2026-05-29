package com.andeva.atelier.platform.operations.domain.model.valueobjects;

public enum WorkOrderStatus {
    PENDING {
        @Override
        public boolean canTransitionTo(WorkOrderStatus next) {
            return next == IN_PROGRESS;
        }
    },
    IN_PROGRESS {
        @Override
        public boolean canTransitionTo(WorkOrderStatus next) {
            return next == COMPLETED;
        }
    },
    COMPLETED {
        @Override
        public boolean canTransitionTo(WorkOrderStatus next) {
            return next == PAID || next == IN_PROGRESS;
        }
    },
    PAID {
        @Override
        public boolean canTransitionTo(WorkOrderStatus next) {
            return false;
        }
    };

    private static final String INVALID_TRANSITION_MESSAGE_WORK_ORDER = "operations.error.workOrderStatus.invalidTransition";

    public abstract boolean canTransitionTo(WorkOrderStatus next);

    public WorkOrderStatus transitionTo(WorkOrderStatus next) {
        if (!canTransitionTo(next)) {
            throw new IllegalStateException(INVALID_TRANSITION_MESSAGE_WORK_ORDER);
        }
        return next;
    }
}
