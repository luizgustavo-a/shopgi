package tech.shopgi.customerms.model.exception;

public class CostumerNotFoundException extends Exception {
    public CostumerNotFoundException() {
        super("Costumer not found.");
    }
}
