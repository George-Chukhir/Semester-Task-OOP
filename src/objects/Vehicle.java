package objects;

import java.util.Objects;

public class Vehicle {
    private final String licensePlate;
    private final int originalValue;

    public Vehicle(String licensePlate, int originalValue) {
        if (licensePlate == null || licensePlate.length() != 7 || originalValue <= 0 ||
            !licensePlate.chars().allMatch(ch -> Character.isDigit(ch) || (ch >= 'A' && ch <= 'Z')))
        {
            throw new IllegalArgumentException("License plate is invalid or originalValue is invalid");
        }


        this.licensePlate = licensePlate;
        this.originalValue = originalValue;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getOriginalValue() {
        return originalValue;
    }


}
