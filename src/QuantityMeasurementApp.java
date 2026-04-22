package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * QuantityMeasurementAppUC4 - Extended Unit Support
 * This version adds support for Yards and Centimeters while maintaining 
 * the DRY principle and backward compatibility with UC3.
 */
public class QuantityMeasurementApp {

    // --- EXTENDED LENGTH CLASS ---

    public static class Length {
        private final double value;
        private final LengthUnit unit;

        /**
         * Enum representing length units with conversion factors relative to Inches.
         * FEET = 12 inches
         * INCHES = 1 inch
         * YARDS = 36 inches
         * CENTIMETERS = 0.393701 inches
         */
        public enum LengthUnit {
            FEET(12.0),
            INCHES(1.0),
            YARDS(36.0),
            CENTIMETERS(0.393701);

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
         * Converts value to base unit (inches) and rounds to 2 decimal places 
         * to handle floating-point precision issues in comparisons.
         */
        private double convertToBaseUnit() {
            double rawValue = value * unit.getConversionFactor();
            return Math.round(rawValue * 100.0) / 100.0;
        }

        /**
         * Compares two Length objects based on their base unit values.
         */
        public boolean compare(Length thatLength) {
            if (thatLength == null) return false;
            return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
        }

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
     * Demonstrates comparison between two values and prints the result.
     */
    public static boolean demonstrateLengthComparison(double val1, Length.LengthUnit unit1, 
                                                     double val2, Length.LengthUnit unit2) {
        Length length1 = new Length(val1, unit1);
        Length length2 = new Length(val2, unit2);
        boolean isEqual = length1.equals(length2);
        
        System.out.println(val1 + " " + unit1 + " == " + val2 + " " + unit2 + " is: " + isEqual);
        return isEqual;
    }

    // --- MAIN METHOD ---

    public static void main(String[] args) {
        System.out.println("=== UC4: Extended Unit Support (Feet, Inches, Yards, CM) ===\n");

        // 1. Feet and Inches Comparison
        demonstrateLengthComparison(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES);

        // 2. Yards and Inches Comparison
        demonstrateLengthComparison(1.0, Length.LengthUnit.YARDS, 36.0, Length.LengthUnit.INCHES);

        // 3. Centimeters and Inches Comparison
        demonstrateLengthComparison(100.0, Length.LengthUnit.CENTIMETERS, 39.3701, Length.LengthUnit.INCHES);

        // 4. Feet and Yards Comparison
        demonstrateLengthComparison(3.0, Length.LengthUnit.FEET, 1.0, Length.LengthUnit.YARDS);

        // 5. Centimeters and Feet Comparison
        demonstrateLengthComparison(30.48, Length.LengthUnit.CENTIMETERS, 1.0, Length.LengthUnit.FEET);
    }
}