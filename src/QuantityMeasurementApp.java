package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * UC9 - QuantityMeasurementApp: Full Multi-Category Support
 * Supports: Length, Volume, Weight, and Temperature.
 * Handles additive units (Length/Volume/Weight) and non-additive offset units (Temperature).
 */
public class QuantityMeasurementApp {

    public static class Quantity {
        private final double value;
        private final Unit unit;

        /**
         * Enum defining all supported units, their base factors, and categories.
         * For Temperature, factors are used for ratio, and base is Celsius.
         */
        public enum Unit {
            // LENGTH (Base: Inches)
            FEET(12.0, Category.LENGTH), INCHES(1.0, Category.LENGTH), 
            YARDS(36.0, Category.LENGTH), CM(0.4537, Category.LENGTH),

            // VOLUME (Base: Litres)
            GALLON(3.78, Category.VOLUME), LITRE(1.0, Category.VOLUME), ML(0.001, Category.VOLUME),

            // WEIGHT (Base: Grams)
            KG(1000.0, Category.WEIGHT), GRAMS(1.0, Category.WEIGHT), TONNE(1000000.0, Category.WEIGHT),

            // TEMPERATURE (Base: Celsius)
            FAHRENHEIT(1.0, Category.TEMPERATURE), CELSIUS(1.0, Category.TEMPERATURE);

            public final double factor;
            public final Category category;

            Unit(double factor, Category category) {
                this.factor = factor;
                this.category = category;
            }
        }

        public enum Category { LENGTH, VOLUME, WEIGHT, TEMPERATURE }

        public Quantity(double value, Unit unit) {
            this.value = value;
            this.unit = unit;
        }

        /**
         * Converts value to category base unit.
         * Special handling for Fahrenheit to Celsius conversion.
         */
        private double convertToBase() {
            if (unit == Unit.FAHRENHEIT) {
                return (value - 32) * 5 / 9;
            }
            return value * unit.factor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Quantity that = (Quantity) o;
            if (this.unit.category != that.unit.category) return false;

            double v1 = convertToBase();
            double v2 = that.convertToBase();
            return Double.compare(Math.round(v1 * 100.0) / 100.0, Math.round(v2 * 100.0) / 100.0) == 0;
        }

        /**
         * Adds two quantities. Addition is only allowed for additive categories.
         */
        public Quantity add(Quantity that, Unit target) {
            if (this.unit.category == Category.TEMPERATURE) {
                throw new IllegalArgumentException("Temperature addition is not physically meaningful.");
            }
            if (this.unit.category != that.unit.category || this.unit.category != target.category) {
                throw new IllegalArgumentException("Category mismatch");
            }
            double totalBase = this.convertToBase() + that.convertToBase();
            return new Quantity(Math.round((totalBase / target.factor) * 100.0) / 100.0, target);
        }

        @Override
        public String toString() { return value + " " + unit; }
    }

    public static void main(String[] args) {
        System.out.println("=== UC9: Weight & Temperature Support ===");

        // 1. Weight Equality: 1 KG == 1000 Grams
        Quantity kg = new Quantity(1.0, Quantity.Unit.KG);
        Quantity g = new Quantity(1000.0, Quantity.Unit.GRAMS);
        System.out.println("1 KG == 1000 Grams: " + kg.equals(g));

        // 2. Weight Addition: 1 Tonne + 1000 Grams = 1001 KG
        Quantity tonne = new Quantity(1.0, Quantity.Unit.TONNE);
        System.out.println("1 Tonne + 1000g in KG: " + tonne.add(g, Quantity.Unit.KG));

        // 3. Temperature: 212 F == 100 C
        Quantity fahr = new Quantity(212.0, Quantity.Unit.FAHRENHEIT);
        Quantity cel = new Quantity(100.0, Quantity.Unit.CELSIUS);
        System.out.println("212 F == 100 C: " + fahr.equals(cel));

        // 4. Temperature: 32 F == 0 C
        System.out.println("32 F == 0 C: " + new Quantity(32, Quantity.Unit.FAHRENHEIT).equals(new Quantity(0, Quantity.Unit.CELSIUS)));
    }
}