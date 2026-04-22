package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * QuantityMeasurementAppUC3 - Unified Quantity Measurement System
 * This refactoring eliminates code duplication by using a single Length class
 * and an Enum to manage different units and their conversion factors.
 */
public class QuantityMeasurementApp {

    // --- UNIFIED LENGTH CLASS ---

    public static class Length {
        private final double value;
        private final LengthUnit unit;

        /**
         * Enum to represent different length units and their conversion factors.
         * Base unit is INCHES (1.0).
         */
        public enum LengthUnit {
            FEET(12.0), 
            INCHES(1.0);

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
         * Converts the current length value to the base unit (inches).
         */
        private double convertToBaseUnit() {
            return value * unit.getConversionFactor();
        }

        /**
         * Compares two Length objects for equality based on their base unit values.
         */
        public boolean compare(Length thatLength) {
            if (thatLength == null) return false;
            return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
        }

        /**
         * Overridden equals to handle reference, null, and class checks
         * before calling the unit-aware compare method.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length length = (Length) o;
            return this.compare(length);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, unit);
        }
    }

    // --- DEMONSTRATION METHODS ---

    /**
     * Generic method to demonstrate equality between two Length objects.
     */
    public static boolean demonstrateLengthEquality(Length length1, Length length2) {
        return length1.equals(length2);
    }

    public static void demonstrateFeetEquality() {
        Length feet1 = new Length(1.0, Length.LengthUnit.FEET);
        Length feet2 = new Length(1.0, Length.LengthUnit.FEET);
        System.out.println("1 Feet == 1 Feet: " + demonstrateLengthEquality(feet1, feet2));
    }

    public static void demonstrateInchesEquality() {
        Length inch1 = new Length(12.0, Length.LengthUnit.INCHES);
        Length inch2 = new Length(12.0, Length.LengthUnit.INCHES);
        System.out.println("12 Inches == 12 Inches: " + demonstrateLengthEquality(inch1, inch2));
    }

    /**
     * UC3 Special: Demonstrates comparison BETWEEN different units (Feet vs Inches).
     */
    public static void demonstrateFeetInchesComparison() {
        Length oneFoot = new Length(1.0, Length.LengthUnit.FEET);
        Length twelveInches = new Length(12.0, Length.LengthUnit.INCHES);
        
        System.out.println("--- Cross-Unit Comparison ---");
        System.out.println("1 Foot == 12 Inches: " + demonstrateLengthEquality(oneFoot, twelveInches));
    }

    // --- MAIN METHOD ---

    public static void main(String[] args) {
        System.out.println("=== UC3: Unified Measurement System ===");
        demonstrateFeetEquality();
        demonstrateInchesEquality();
        demonstrateFeetInchesComparison();
    }
}
