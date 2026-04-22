package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * QuantityMeasurementApp - UC2: Inches measurement equality
 * This class is responsible for checking the equality of numerical values
 * measured in feet and inches.
 */
public class QuantityMeasurementApp {

    // --- INNER CLASSES ---

    /**
     * Inner class to represent Feet measurement
     */
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet feet = (Feet) obj;
            return Double.compare(feet.value, value) == 0;
        }
    }

    /**
     * Inner class to represent Inches measurement
     */
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches inches = (Inches) obj;
            return Double.compare(inches.value, value) == 0;
        }
    }

    // --- DEMONSTRATION METHODS ---

    public static void demonstrateFeetEquality() {
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        System.out.println("Feet Equality (1.0 == 1.0): " + f1.equals(f2));
    }

    public static void demonstrateInchesEquality() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(1.0);
        System.out.println("Inches Equality (1.0 == 1.0): " + i1.equals(i2));
    }

    public static void main(String[] args) {
        demonstrateFeetEquality();
        demonstrateInchesEquality();
    }

    // --- JUNIT TEST CASES (Consolidated from the second image) ---

    public static class QuantityMeasurementAppTest {

        // --- Feet Tests ---
        @Test
        public void testFeetEquality_SameValue() {
            assertEquals(new Feet(0.0), new Feet(0.0));
        }

        @Test
        public void testFeetEquality_DifferentValue() {
            assertNotEquals(new Feet(0.0), new Feet(1.0));
        }

        @Test
        public void testFeetEquality_NullComparison() {
            assertNotEquals(new Feet(0.0), null);
        }

        @Test
        public void testFeetEquality_DifferentClass() {
            assertNotEquals(new Feet(0.0), new Object());
        }

        @Test
        public void testFeetEquality_SameReference() {
            Feet f1 = new Feet(0.0);
            assertEquals(f1, f1);
        }

        // --- Inches Tests ---
        @Test
        public void testInchesEquality_SameValue() {
            assertEquals(new Inches(0.0), new Inches(0.0));
        }

        @Test
        public void testInchesEquality_DifferentValue() {
            assertNotEquals(new Inches(0.0), new Inches(1.0));
        }

        @Test
        public void testInchesEquality_NullComparison() {
            assertNotEquals(new Inches(0.0), null);
        }

        @Test
        public void testInchesEquality_DifferentClass() {
            assertNotEquals(new Inches(0.0), new Object());
        }

        @Test
        public void testInchesEquality_SameReference() {
            Inches i1 = new Inches(0.0);
            assertEquals(i1, i1);
        }
    }
}