package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * IMeasurable interface defines the contract for measurable units.
 *
 * This interface serves as a common abstraction for different types of measurements
 * such as weight and length units. Classes implementing this interface should provide
 * functionality to handle unit conversions and comparisons between different measurement types.
 *
 * @see WeightUnit
 * @see LengthUnit
 */
interface IMeasurable {
    /**
     * Get the conversion factor to the base unit (grams or inches).
     *
     * @return the conversion factor
     */
    double getConversionFactor();

    /**
     * Convert value from this unit to base unit. New responsibility added.
     * <p> This method is used internally for all conversions. It ensures consistent
     * rounding to two decimal places across all operations.
     *
     * @param value the value in this unit
     * @return the value converted to base unit and then rounded to two decimal places
     */
    double convertToBaseUnit(double value);

    /**
     * Convert value from base unit to this unit. New responsibility added.
     * <p> This method is used internally for all conversions. It ensures consistent
     * rounding to two decimal places across all operations.
     *
     * @param baseValue the value in base unit
     * @return the value converted to this unit and then rounded to two decimal places
     */
    double convertFromBaseUnit(double baseValue);
}

// --- LengthUnit Enum ---

/**
 * LengthUnit.java
 * The LengthUnit enumeration implements IMeasurable interface and
 * provides methods for unit conversion. It defines various units of length
 * measurement along with their conversion factors relative to a base unit (inches).
 */
enum LengthUnit implements IMeasurable {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return Math.round((value * conversionFactor) * 100.0) / 100.0;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return Math.round((baseValue / conversionFactor) * 100.0) / 100.0;
    }
}

// --- WeightUnit Enum ---

/**
 * WeightUnit.java
 * The WeightUnit enumeration implements IMeasurable interface and provides
 * methods for unit conversion. It defines various units of weight measurement
 * along with their conversion factors relative to a base unit (grams).
 */
enum WeightUnit implements IMeasurable {
    MILLIGRAM(0.001),
    GRAM(1.0),
    KILOGRAM(1000.0),
    POUND(453.592),
    TONNE(1000000.0);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return Math.round((value * conversionFactor) * 100.0) / 100.0;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return Math.round((baseValue / conversionFactor) * 100.0) / 100.0;
    }
}

// --- Quantity Class ---

/**
 * Represents a quantity with a numeric value and a unit of measurement.
 * This class encapsulates a numeric value along with an associated measurable unit.
 *
 * @author Developer
 * @version 1.0
 */
class Quantity<U extends IMeasurable> {
    private double value;
    private U unit;

    public Quantity(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    /**
     * Converts this Quantity to the specified target unit.
     */
    public <T extends IMeasurable> double convertTo(T targetUnit) {
        double baseValue = unit.convertToBaseUnit(this.value);
        return targetUnit.convertFromBaseUnit(baseValue);
    }

    /**
     * Adds this Quantity to another Quantity of the same unit type.
     */
    public Quantity<U> add(Quantity<U> other) {
        double baseSum = this.unit.convertToBaseUnit(this.value) + 
                         other.unit.convertToBaseUnit(other.value);
        double finalValue = this.unit.convertFromBaseUnit(baseSum);
        return new Quantity<>(finalValue, this.unit);
    }

    /**
     * Adds this Quantity to another Quantity and returns the result in a target unit.
     */
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double baseSum = this.unit.convertToBaseUnit(this.value) + 
                         other.unit.convertToBaseUnit(other.value);
        double finalValue = targetUnit.convertFromBaseUnit(baseSum);
        return new Quantity<>(finalValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity)) return false;
        Quantity<?> other = (Quantity<?>) obj;
        
        double thisBase = unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);
        
        return Math.abs(thisBase - otherBase) < 0.01;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.convertToBaseUnit(value));
    }
}

// --- Main Application Class ---

/**
 * QuantityMeasurementApp - UC10 - Generic Quantity Class with Unit Interface
 * for Multi-Category Support.
 */
public class QuantityMeasurementApp {

    public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> q1, Quantity<U> q2) {
        return q1.equals(q2);
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity, U targetUnit) {
        double newValue = quantity.convertTo(targetUnit);
        return new Quantity<>(newValue, targetUnit);
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2) {
        return q1.add(q2);
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        return q1.add(q2, targetUnit);
    }

    public static void main(String[] args) {
        // Demonstration equality
        Quantity<WeightUnit> weightInGrams = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> weightInKilograms = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        boolean areEqual = demonstrateEquality(weightInGrams, weightInKilograms);
        System.out.println("Are weights equal? " + areEqual);

        // Demonstration conversion
        Quantity<WeightUnit> convertedWeight = demonstrateConversion(weightInGrams, WeightUnit.KILOGRAM);
        System.out.println("Converted Weight: " + convertedWeight.getValue() + " " + convertedWeight.getUnit());

        // Demonstration addition (First unit)
        Quantity<WeightUnit> weightInPounds = new Quantity<>(2.20462, WeightUnit.POUND);
        Quantity<WeightUnit> sumWeight = demonstrateAddition(weightInKilograms, weightInPounds);
        System.out.println("Sum Weight: " + sumWeight.getValue() + " " + sumWeight.getUnit());

        // Demonstration addition (Specified unit)
        Quantity<WeightUnit> sumWeightInGrams = demonstrateAddition(weightInKilograms, weightInPounds, WeightUnit.GRAM);
        System.out.println("Sum Weight in Grams: " + sumWeightInGrams.getValue() + " " + sumWeightInGrams.getUnit());
        
        // Length Example
        Quantity<LengthUnit> lengthInFeet = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> lengthInInches = new Quantity<>(120.0, LengthUnit.INCHES);
        System.out.println("Are lengths equal? " + lengthInFeet.equals(lengthInInches));
    }
}
