package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * UC5 - QuantityMeasurementApp: Extended Unit Support with Conversion
 * This class provides unit-to-unit conversion within the length category.
 * It builds on UC4 by adding explicit conversion logic (convertTo).
 */
public class QuantityMeasurementApp {

    // --- REFACTORED LENGTH CLASS WITH CONVERSION ---

    public static class Length {
        private final double value;
        private final LengthUnit unit;

        /**
         * Nested enumeration representing different length units and their factors.
         * Base unit for conversion is inches.
         */
        public enum LengthUnit {
            FEET(12.0),
            INCHES(1.0),
            YARDS(36.0),
            CENTIMETERS(0.453701); // Factor from your previous UC snippet

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
         * Private Utility Method: Converts length to base unit (inches) with rounding.
         * Ensures consistent rounding to two decimal places.
         */
        private double convertToBaseUnit() {
            double inches = value * unit.getConversionFactor();
            return Math.round(inches * 100.0) / 100.0;
        }

        /**
         * Private Helper Method: Core comparison logic.
         */
        private boolean compare(Length thatLength) {
            if (thatLength == null) return false;
            return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
        }

        /**
         * Overridden Equals: Implements reference, type, and value-based checks.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length length = (Length) o;
            return this.compare(length);
        }

        /**
         * Public API Method: Provides the primary interface for unit conversion.
         * Pipeline: Instance -> Base Unit (Inches) -> Target Unit -> Rounded Result.
         */
        public Length convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            // 1. Convert this instance to inches
            double inches = value * unit.getConversionFactor();
            // 2. Convert from inches to target unit
            double convertedValue = inches / targetUnit.getConversionFactor();
            // 3. Round to two decimal places
            double roundedValue = Math.round(convertedValue * 100.0) / 100.0;
            
            return new Length(roundedValue, targetUnit);
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

    public static boolean demonstrateLengthEquality(Length length1, Length length2) {
        return length1.equals(length2);
    }

    /**
     * Demonstrates length conversion from one unit to another.
     */
    public static Length demonstrateLengthConversion(double value, Length.LengthUnit fromUnit, Length.LengthUnit toUnit) {
        Length length = new Length(value, fromUnit);
        return length.convertTo(toUnit);
    }

    /**
     * Method Overloading: Demonstrates conversion from an existing Length instance.
     */
    public static Length demonstrateLengthConversion(Length length, Length.LengthUnit toUnit) {
        return length.convertTo(toUnit);
    }

    // --- MAIN METHOD ---

    public static void main(String[] args) {
        System.out.println("=== UC5: Extended Unit Support with Conversion ===\n");

        // 1. Demonstrate Conversion: 3 Feet to Inches
        Length threeFeet = new Length(3.0, Length.LengthUnit.FEET);
        Length convertedInches = threeFeet.convertTo(Length.LengthUnit.INCHES);
        System.out.println("Conversion: " + threeFeet + " => " + convertedInches); // Expected: 36.00 INCHES

        // 2. Demonstrate Conversion: 2 Yards to Inches
        Length twoYards = new Length(2.0, Length.LengthUnit.YARDS);
        System.out.println("Conversion: " + twoYards + " => " + twoYards.convertTo(Length.LengthUnit.INCHES)); // Expected: 72.00 INCHES

        // 3. Demonstrate Equality after Conversion
        Length oneFoot = new Length(1.0, Length.LengthUnit.FEET);
        Length twelveInches = new Length(12.0, Length.LengthUnit.INCHES);
        System.out.println("\nEquality Check (1ft vs 12in): " + oneFoot.equals(twelveInches));
    }
}
