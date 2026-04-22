package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * UC8 - QuantityMeasurementApp: Multi-Category Support (Length & Volume)
 * This version introduces Volume units (Gallon, Litre, ML) and prevents
 * cross-category operations (e.g., adding Litres to Feet).
 */
public class QuantityMeasurementApp {

    // --- ENHANCED MEASUREMENT CLASS ---

    public static class Quantity {
        private final double value;
        private final Unit unit;

        /**
         * Enum to define Unit Categories and their base conversion factors.
         */
        public enum Unit {
            // Length Category (Base: Inches)
            FEET(12.0, UnitType.LENGTH),
            INCHES(1.0, UnitType.LENGTH),
            YARDS(36.0, UnitType.LENGTH),
            CENTIMETERS(0.4537, UnitType.LENGTH),

            // Volume Category (Base: Litres)
            GALLON(3.78, UnitType.VOLUME),
            LITRE(1.0, UnitType.VOLUME),
            ML(0.001, UnitType.VOLUME);

            public final double factor;
            public final UnitType type;

            Unit(double factor, UnitType type) {
                this.factor = factor;
                this.type = type;
            }
        }

        public enum UnitType { LENGTH, VOLUME }

        public Quantity(double value, Unit unit) {
            this.value = value;
            this.unit = unit;
        }

        /**
         * Converts value to its respective base unit (Inches or Litres).
         */
        private double convertToBaseUnit() {
            return value * unit.factor;
        }

        /**
         * Compares two quantities for equality.
         * Includes a check to ensure units belong to the same category.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Quantity that = (Quantity) o;
            
            // Prevent comparing Length to Volume
            if (this.unit.type != that.unit.type) return false;

            return Double.compare(Math.round(this.convertToBaseUnit() * 100.0) / 100.0, 
                                  Math.round(that.convertToBaseUnit() * 100.0) / 100.0) == 0;
        }

        /**
         * Adds two quantities and returns result in the specified target unit.
         * Throws IllegalArgumentException if categories don't match.
         */
        public Quantity add(Quantity that, Unit targetUnit) {
            if (this.unit.type != that.unit.type || this.unit.type != targetUnit.type) {
                throw new IllegalArgumentException("Incompatible Unit Categories");
            }
            double totalBaseValue = this.convertToBaseUnit() + that.convertToBaseUnit();
            double resultValue = Math.round((totalBaseValue / targetUnit.factor) * 100.0) / 100.0;
            return new Quantity(resultValue, targetUnit);
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

    // --- MAIN METHOD ---

    public static void main(String[] args) {
        System.out.println("=== UC8: Multi-Category Support (Length & Volume) ===\n");

        // 1. Volume Equality: 1 Gallon == 3.78 Litres
        Quantity gallon = new Quantity(1.0, Quantity.Unit.GALLON);
        Quantity litres = new Quantity(3.78, Quantity.Unit.LITRE);
        System.out.println("1 Gallon == 3.78 Litres: " + gallon.equals(litres));

        // 2. Volume Addition: 1 Gallon + 3.78 Litres = 7.56 Litres
        Quantity sumVolume = gallon.add(litres, Quantity.Unit.LITRE);
        System.out.println("Addition (Result in Litres): " + sumVolume);

        // 3. Volume Addition: 1 Litre + 1000 ML = 2 Litres
        Quantity litre = new Quantity(1.0, Quantity.Unit.LITRE);
        Quantity ml = new Quantity(1000.0, Quantity.Unit.ML);
        System.out.println("1 Litre + 1000 ML = " + litre.add(ml, Quantity.Unit.LITRE));

        // 4. Category Protection Example (Length vs Volume)
        Quantity foot = new Quantity(1.0, Quantity.Unit.FEET);
        System.out.println("\nEquality Check (1 Foot vs 1 Gallon): " + foot.equals(gallon));
        
        try {
            foot.add(gallon, Quantity.Unit.FEET);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage()); // Expected: Incompatible Unit Categories
        }
    }
}