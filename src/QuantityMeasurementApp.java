package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * UC7 - QuantityMeasurementApp: Addition with Target Unit Specification
 * This version introduces the ability to add two lengths and specify the 
 * output unit for the result, further reducing code duplication.
 */
public class QuantityMeasurementApp {

    // --- ENHANCED LENGTH CLASS ---

    public static class Length {
        private final double value;
        private final LengthUnit unit;

        public enum LengthUnit {
            FEET(12.0),
            INCHES(1.0),
            YARDS(36.0),
            CENTIMETERS(0.453701);

            private final double conversionFactor;

            LengthUnit(double conversionFactor) {
                this.conversionFactor = conversionFactor;
            }

            public double getConversionFactor() {
                return conversionFactor;
            }
        }

        public Length(double value, LengthUnit unit) {
            this.value = value;
            this.unit = unit;
        }

        /**
         * Converts current length to base unit (inches).
         */
        private double convertToBaseUnit() {
            return value * unit.getConversionFactor();
        }

        /**
         * Private utility to convert an inch value to a target unit with rounding.
         */
        private double convertFromBaseToTargetUnit(double lengthInInches, LengthUnit targetUnit) {
            double convertedValue = lengthInInches / targetUnit.getConversionFactor();
            return Math.round(convertedValue * 100.0) / 100.0;
        }

        /**
         * UC7 Core Logic: Internal helper to sum and convert.
         * Used by both add methods to maintain DRY principles.
         */
        private Length addAndConvert(Length thatLength, LengthUnit targetUnit) {
            double totalInches = this.convertToBaseUnit() + thatLength.convertToBaseUnit();
            double resultValue = convertFromBaseToTargetUnit(totalInches, targetUnit);
            return new Length(resultValue, targetUnit);
        }

        /**
         * UC6 Compatibility: Adds another length, returning result in 'this' unit.
         */
        public Length add(Length thatLength) {
            return addAndConvert(thatLength, this.unit);
        }

        /**
         * UC7 New Feature: Adds another length, returning result in the 'targetUnit'.
         */
        public Length add(Length thatLength, LengthUnit targetUnit) {
            return addAndConvert(thatLength, targetUnit);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length length = (Length) o;
            return Double.compare(Math.round(this.convertToBaseUnit() * 100.0) / 100.0, 
                                  Math.round(length.convertToBaseUnit() * 100.0) / 100.0) == 0;
        }

        @Override
        public String toString() {
            return String.format("%.2f %s", value, unit);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, unit);
        }
    }

    // --- DEMONSTRATION METHODS ---

    /**
     * Demonstrates addition with an explicit target unit.
     */
    public static void demonstrateAdditionWithTarget(Length l1, Length l2, Length.LengthUnit target) {
        Length result = l1.add(l2, target);
        System.out.println("Addition: (" + l1 + ") + (" + l2 + ") in " + target + " = " + result);
    }

    // --- MAIN METHOD ---

    public static void main(String[] args) {
        System.out.println("=== UC7: Addition with Target Unit Specification ===\n");

        Length oneFoot = new Length(1.0, Length.LengthUnit.FEET);
        Length twelveInches = new Length(12.0, Length.LengthUnit.INCHES);

        // 1. Result in Feet (UC6 style)
        System.out.println("Result in First Operand Unit:");
        System.out.println("Sum: " + oneFoot.add(twelveInches)); // 2.00 FEET

        // 2. Result in Inches (UC7 style)
        System.out.println("\nResult in Specified Target Unit:");
        demonstrateAdditionWithTarget(oneFoot, twelveInches, Length.LengthUnit.INCHES); // 24.00 INCHES

        // 3. Complex addition to Yards
        Length twoFeet = new Length(2.0, Length.LengthUnit.FEET);
        Length oneYard = new Length(1.0, Length.LengthUnit.YARDS);
        demonstrateAdditionWithTarget(twoFeet, oneYard, Length.LengthUnit.YARDS); // 1.67 YARDS
    }
}