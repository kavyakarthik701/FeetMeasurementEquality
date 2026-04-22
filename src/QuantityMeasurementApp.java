package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * QuantityMeasurementApp - UC1: Feet measurement equality
 * This class handles the logic for representing feet and checking equality.
 */
public class QuantityMeasurementApp {

    /**
     * Inner class to represent Feet measurement.
     * Encapsulates a double value and overrides equals for comparison.
     */
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        /**
         * Overrides equals() method to compare two Feet objects.
         * Logic follows these steps:
         * 1. Reference Check: Same memory address?
         * 2. Null Check: Is the other object null?
         * 3. Type Check: Is the other object a Feet instance?
         * 4. Value Comparison: Do the primitive values match?
         */
        @Override
        public boolean equals(Object obj) {
            // 1. Reference Check
            if (this == obj) return true;

            // 2. Null Check and 3. Type Check
            if (obj == null || getClass() != obj.getClass()) return false;

            // 4. Value Comparison (casting obj to Feet)
            Feet feet = (Feet) obj;
            return Double.compare(feet.value, value) == 0;
        }
    }

    // --- JUNIT TEST CASES (Consolidated from the second image) ---

    public static class QuantityMeasurementAppTest {

        @Test
        public void testFeetEquality_SameValue() {
            Feet f1 = new Feet(0.0);
            Feet f2 = new Feet(0.0);
            assertEquals(f1, f2, "Two Feet objects with the same value should be equal.");
        }

        @Test
        public void testFeetEquality_DifferentValue() {
            Feet f1 = new Feet(0.0);
            Feet f2 = new Feet(1.0);
            assertNotEquals(f1, f2, "Two Feet objects with different values should not be equal.");
        }

        @Test
        public void testFeetEquality_NullComparison() {
            Feet f1 = new Feet(0.0);
            assertNotEquals(null, f1, "A Feet object should not be equal to null.");
        }

        @Test
        public void testFeetEquality_DifferentClass() {
            Feet f1 = new Feet(0.0);
            Object obj = new Object();
            assertNotEquals(f1, obj, "A Feet object should not be equal to an object of a different class.");
        }

        @Test
        public void testFeetEquality_SameReference() {
            Feet f1 = new Feet(0.0);
            assertEquals(f1, f1, "An object should be equal to its own reference.");
        }
    }

    /**
     * Main method to demonstrate Feet equality check manually.
     */
    public static void main(String[] args) {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);

        System.out.println("Checking equality for 1.0 feet and 1.0 feet...");
        System.out.println("Result: " + feet1.equals(feet2));
    }
}