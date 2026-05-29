package com.andeva.atelier.platform.shared.domain.model.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private static final String NOT_NULL_MESSAGE_KEY = "operations.error.money.required";
    private static final String NOT_NEGATIVE_MESSAGE_KEY = "operations.error.money.cannotBeNegative";

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException(NOT_NULL_MESSAGE_KEY);
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(NOT_NEGATIVE_MESSAGE_KEY);
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Money plus(Money other) {
        if (other == null) return this;
        return new Money(this.amount.add(other.amount));
    }

    public Money minus(Money other) {
        if (other == null) return this;
        return new Money(this.amount.subtract(other.amount));
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null) return this;
        return new Money(this.amount.multiply(factor));
    }

    public boolean isGreaterThan(Money other) {
        return other != null && this.amount.compareTo(other.amount) > 0;
    }
    public boolean isLessThan(Money other) {
        return other != null && this.amount.compareTo(other.amount) < 0;
    }
}
