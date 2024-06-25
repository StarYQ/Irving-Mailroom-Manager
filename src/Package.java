/**
 * Name: Arnab Bhowmik
 * ID: 115610923
 * Recitation Section: 1
 */

/**
 * Represents a mail package object with specified recipient information
 */
public class Package {
    private String recipient;
    private int arrivalDate;
    private double weight;

    /**
     * Constructs a Package with specified recipient, arrival date, and weight
     *
     * @param recipient   The name of the recipient
     * @param arrivalDate The arrival date of the package
     * @param weight      The weight of the package
     */
    public Package(String recipient, int arrivalDate, double weight) {
        this.recipient = recipient;
        this.arrivalDate = arrivalDate;
        this.weight = weight;
    }

    /**
     * Gets the name of the recipient
     *
     * @return The name of the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Gets the arrival date of the package
     *
     * @return The arrival date of the package
     */
    public int getArrivalDate() {
        return arrivalDate;
    }

    /**
     * Gets the weight of the package
     *
     * @return The weight of the package
     */
    public double getWeight() {
        return weight;
    }
}