package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * IMeasurable interface defines the contract for all measurement units.
 * It ensures that different types of measurements (Length, Weight, Volume) 
 * can be handled by the same generic Quantity class.
 */
interface IMeasurable {
    double getConversionFactor(); //
    double convertToBaseUnit(double value); //
    double convertFromBaseUnit(double baseValue); //
}

// --- UNIT ENUMERATIONS ---

/**
 * Length units based on inches.
 */
enum LengthUnit implements IMeasurable {
    FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701); //
    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }
    public double getConversionFactor() { return factor; }
    public double convertToBaseUnit(double v) { return Math.round((v * factor) * 100.0) / 100.0; } //
    public double convertFromBaseUnit(double b) { return Math.round((b / factor) * 100.0) / 100.0; } //
}

/**
 * Weight units based on grams.
 */
enum WeightUnit implements IMeasurable {
    MILLIGRAM(0.001), GRAM(1.0), KILOGRAM(1000.0), POUND(453.592), TONNE(1000000.0); //
    private final double factor;
    WeightUnit(double factor) { this.factor = factor; }
    public double getConversionFactor() { return factor; }
    public double convertToBaseUnit(double v) { return Math.round((v * factor) * 100.0) / 100.0; } //
    public double convertFromBaseUnit(double b) { return Math.round((b / factor) * 100.0) / 100.0; } //
}

/**
 * Volume units based on Liters (required for tests in image_f3639e.jpg).
 */
enum VolumeUnit implements IMeasurable {
    LITER(1.0), MILLILITER(0.001), GALLON(3.785); 
    private final double factor;
    VolumeUnit(double factor) { this.factor = factor; }
    public double getConversionFactor() { return factor; }
    public double convertToBaseUnit(double v) { return Math.round((v * factor) * 100.0) / 100.0; }
    public double convertFromBaseUnit(double b) { return Math.round((b / factor) * 100.0) / 100.0; }
}

// --- GENERIC QUANTITY CLASS ---

/**
 * The Generic Quantity class prevents cross-type operations.
 * For example, a Quantity<LengthUnit> cannot be compared to a Quantity<WeightUnit>.
 */
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) { //
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; } //
    public U getUnit() { return unit; } //

    public double convertTo(U targetUnit) { //
        double base = unit.convertToBaseUnit(this.value); //
        return targetUnit.convertFromBaseUnit(base); //
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) { //
        double totalBase = unit.convertToBaseUnit(this.value) + 
                           other.unit.convertToBaseUnit(other.value); //
        return new Quantity<>(targetUnit.convertFromBaseUnit(totalBase), targetUnit); //
    }

    @Override
    public boolean equals(Object obj) { //
        if (this == obj) return true;
        if (!(obj instanceof Quantity)) return false;
        Quantity<?> that = (Quantity<?>) obj;
        
        // Safety check: only compare if they belong to the same Enum type
        if (!this.unit.getClass().equals(that.unit.getClass())) return false; //

        double thisBase = unit.convertToBaseUnit(this.value); //
        double thatBase = ((IMeasurable)that.unit).convertToBaseUnit(that.value); //
        return Math.abs(thisBase - thatBase) < 0.01; //
    }
}

// --- MAIN APPLICATION ---

public class QuantityMeasurementApp {
    public static void main(String[] args) {
        // matches tests in image_f3639e.jpg
        Quantity<VolumeUnit> liters = new Quantity<>(1.0, VolumeUnit.LITER);
        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITER);
        
        System.out.println("Are 1L and 1000ml equal? " + liters.equals(ml)); //
        
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches = new Quantity<>(12.0, LengthUnit.INCHES);
        System.out.println("Are 1ft and 12in equal? " + feet.equals(inches)); //
    }
}
