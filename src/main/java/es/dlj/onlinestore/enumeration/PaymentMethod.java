package es.dlj.onlinestore.enumeration;

public enum PaymentMethod {
    CREDIT_CARD ("Credit Card"),
    PAYPAL ("PayPal"),
    BANK_TRANSFER ("Bank Transfer");

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
