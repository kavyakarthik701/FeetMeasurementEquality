package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * UC6 - QuantityMeasurementApp: Addition Operations Between Lengths
 * This version allows adding two Length objects of the same category.
 * The result is returned in the unit of the first operand.
 */
public class QuantityMeasurementApp {

    // --- REFACTORED LENGTH CLASS WITH ADDITION ---

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
         * Converts current length to inches (base unit).
         */
        private double convertToBaseUnit() {
            return value * unit.getConversionFactor();
        }

        /**
         * UC6 Helper: Converts an inch value back to a specific target unit.
         */
        private double convertFromBaseToTargetUnit(double lengthInInches, LengthUnit targetUnit) {
            double convertedValue = lengthInInches / targetUnit.getConversionFactor();
            return Math.round(convertedValue * 100.0) / 100.0;
        }

        /**
         * UC6 Main Logic: Adds another Length to this one.
         * Addition Pipeline: 
         * 1. Convert both to inches -> 2. Sum them -> 3. Convert back to 'this' unit.
         */
        public Length add(Length thatLength) {
            double totalInches = this.convertToBaseUnit() + thatLength.convertToBaseUnit();
            double resultValue = convertFromBaseToTargetUnit(totalInches, this.unit);
            return new Length(resultValue, this.unit);
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
     * Demonstrates addition of two lengths.
     */
    public static Length demonstrateLengthAddition(Length l1, Length l2) {
        Length result = l1.add(l2);
        System.out.println("Addition: (" + l1 + ") + (" + l2 + ") = " + result);
        return result;
    }

    // --- MAIN METHOD ---

    public static void main(String[] args) {
        System.out.println("=== UC6: Addition Operations Between Measurements ===\n");

        // Example 1: 1 Foot + 12 Inches = 2.00 FEET
        Length foot = new Length(1.0, Length.LengthUnit.FEET);
        Length inches = new Length(12.0, Length.LengthUnit.INCHES);
        demonstrateLengthAddition(foot, inches);

        // Example 2: 2 Inches + 5 Centimeters
        Length in = new Length(2.0, Length.LengthUnit.INCHES);
        Length cm = new Length(5.0, Length.LengthUnit.CENTIMETERS);
        demonstrateLengthAddition(in, cm);

        // Example 3: 3 Yards + 3 Feet = 4.00 YARDS
        Length yards = new Length(3.0, Length.LengthUnit.YARDS);
        Length threeFeet = new Length(3.0, Length.LengthUnit.FEET);
        demonstrateLengthAddition(yards, threeFeet);
    }
}