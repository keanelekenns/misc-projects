package ca.uvic.seng330.assn3.models.devices;
/* This is a simple, immutable class
 * designed to contain and return values
 * for the degree of the temperature and
 * the units for the degrees.
 */

public class Temperature {

  private final double aDegrees;
  private final Unit aUnit;

  public static final double MAX_TEMP_CELSIUS = 538;
  public static final double MAX_TEMP_FAHRENHEIT = 1000;

  public Temperature(Unit pUnit, double pDegrees) {
    aDegrees = pDegrees;
    aUnit = pUnit;
  }

  public double getDegrees() {
    return aDegrees;
  }

  public Unit getUnit() {
    return aUnit;
  }

  public enum Unit {
    CELSIUS,
    FAHRENHEIT;

    @Override
    public String toString() {
      switch (this) {
        case CELSIUS:
          return "\u2103";
        case FAHRENHEIT:
          return "\u2109";
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  public boolean equals(Temperature other) {
    if (Double.compare(aDegrees, other.aDegrees) == 0 && aUnit.equals(other.aUnit)) {
      return true;
    }
    return false;
  }
}
