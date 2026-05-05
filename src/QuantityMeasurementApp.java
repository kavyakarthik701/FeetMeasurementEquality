package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * Represents a quantity with a numeric value and a unit of measurement.
 * 
 * In UC12, the Quantity class has been enhanced to include additional operations
 * such as subtraction and division.
 * 1. Subtraction and division, along with improved error handling for incompatible
 *    units and division by zero scenarios.
 * 2. The equals method has been overridden to allow for meaningful comparisons between
 *    Quantity objects based on their converted values in base units.
 * 3. The toString method has also been overridden to provide a clear string representation
 *    of the Quantity object.
 * 
 * @author Developer
 * @version 12.0
 */
public class Quantity<U extends IMeasurable> {
    private double value;
    private U unit;

    public Quantity(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public U getUnit() { return unit; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity)) return false;
        Quantity<?> other = (Quantity<?>) obj;
        
        // Ensure units are of the same category before comparing
        if (!this.unit.getClass().equals(other.unit.getClass())) return false;

        double thisBaseValue = this.unit.convertToBaseUnit(this.value);
        double otherBaseValue = ((IMeasurable) other.unit).convertToBaseUnit(other.value);
        return Math.abs(thisBaseValue - otherBaseValue) < 0.01;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit);
    }

    public <T extends IMeasurable> double convertTo(T targetUnit) {
        double baseValue = unit.convertToBaseUnit(this.value);
        return targetUnit.convertFromBaseUnit(baseValue);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double sumInBase = this.unit.convertToBaseUnit(this.value) + 
                           other.unit.convertToBaseUnit(other.value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(sumInBase), targetUnit);
    }

    /**
     * Subtracts this Quantity from another Quantity of the same unit type and 
     * returns the result in a specified target unit.
     */
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        // Logic: other - this (Subtracting "this" FROM "other")
        double diffInBase = other.unit.convertToBaseUnit(other.value) - 
                            this.unit.convertToBaseUnit(this.value);
        
        if (diffInBase < 0) {
            throw new IllegalArgumentException("Subtraction resulted in a negative value.");
        }
        return new Quantity<>(targetUnit.convertFromBaseUnit(diffInBase), targetUnit);
    }

    /**
     * Divides this Quantity by another Quantity of the same unit type and 
     * returns the result as a double.
     */
    public double divide(Quantity<U> other) {
        double divisorBase = other.unit.convertToBaseUnit(other.value);
        if (divisorBase == 0) {
            throw new ArithmeticException("Division by zero occurs");
        }
        return this.unit.convertToBaseUnit(this.value) / divisorBase;
    }
}
