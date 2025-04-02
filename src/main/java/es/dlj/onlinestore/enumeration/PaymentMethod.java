package es.dlj.onlinestore.enumeration;

public enum PaymentMethod {
    CREDIT_CARD ("CREDIT_CARD"),
    PAYPAL ("PAYPAL"),
    BANK_TRANSFER ("BANK_TRANSFER");

    private final String name;

    PaymentMethod(String name) {
        this.name = name;
    }

    public static PaymentMethod fromString(String name) {
        for (PaymentMethod pMethod : PaymentMethod.values()) {
            if (pMethod.name.equalsIgnoreCase(name)) {
                return pMethod;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
