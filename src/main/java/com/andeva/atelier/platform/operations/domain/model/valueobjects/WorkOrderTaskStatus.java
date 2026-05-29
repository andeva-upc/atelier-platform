package com.andeva.atelier.platform.operations.domain.model.valueobjects;

public enum WorkOrderTaskStatus {
    PENDING {
        @Override
        public boolean canTransitionTo(WorkOrderTaskStatus next) {
            return next == DOING;
        }
    },
    DOING {
        @Override
        public boolean canTransitionTo(WorkOrderTaskStatus next) {
            return next == COMPLETED;
        }
    },
    COMPLETED {
        @Override
        public boolean canTransitionTo(WorkOrderTaskStatus next) {
            return next == DOING;
        }
    };

    private static final String INVALID_TRANSITION_MESSAGE_WORK_ORDER_TASK = "operations.error.workOrderTaskStatus.invalidTransition";

    public abstract boolean canTransitionTo(WorkOrderTaskStatus next);

    public WorkOrderTaskStatus transitionTo(WorkOrderTaskStatus next) {
        if (!canTransitionTo(next)) {
            throw new IllegalStateException(INVALID_TRANSITION_MESSAGE_WORK_ORDER_TASK);
        }
        return next;
    }
}
