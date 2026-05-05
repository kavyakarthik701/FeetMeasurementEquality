package com.apps.quantitymeasurement;

import java.util.function.DoubleBinaryOperator;

/**
 * In UC13, the Quantity class ensures DRY principles by centralizing validation
 * and arithmetic logic using a private enum and unified methods.
 */
public class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    // --- Private Helper Enum & Methods ---

    /**
     * Enumeration representing types of arithmetic operations.
     * Uses lambda expressions to define specific computations.
     */
    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> b - a), // Logic: Subtract "this" from "other"
        DIVIDE((a, b) -> {
            if (b == 0) throw new ArithmeticException("Division by zero occurs");
            return a / b;
        });

        private final DoubleBinaryOperator operator;

        ArithmeticOperation(DoubleBinaryOperator operator) {
            this.operator = operator;
        }

        public double compute(double v1, double v2) {
            return operator.applyAsDouble(v1, v2);
        }
    }

    /**
     * Validates NULLs, unit compatibility, and numeric finiteness.
     */
    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {
        if (other == null || (targetUnitRequired && targetUnit == null)) {
            throw new IllegalArgumentException("Operands or target unit cannot be null");
        }
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Units are incompatible for this operation");
        }
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Numeric values must be finite");
        }
    }

    /**
     * Unified method to execute arithmetic on base unit values.
     */
    private double performArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        double v1Base = this.unit.convertToBaseUnit(this.value);
        double v2Base = other.unit.convertToBaseUnit(other.value);
        return operation.compute(v1Base, v2Base);
    }

    // --- Public Arithmetic Interface ---

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double resultBase = performArithmetic(other, ArithmeticOperation.ADD);
        return new Quantity<>(targetUnit.convertFromBaseUnit(resultBase), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double resultBase = performArithmetic(other, ArithmeticOperation.SUBTRACT);
        if (resultBase < 0) throw new IllegalArgumentException("Result cannot be negative");
        return new Quantity<>(targetUnit.convertFromBaseUnit(resultBase), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    // Standard overrides (equals, toString, etc.) omitted for brevity...
}
