package es.dlj.onlinestore.model;

public enum ProductType {
    NEW ("NEW"),
    RECONDITIONED ("RECONDITIONED"),
    SECONDHAND ("SECONDHAND");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ProductType fromString(String name) {
        for (ProductType pType : ProductType.values()) {
            if (pType.name.equalsIgnoreCase(name)) {
                return pType;
            }
        }
        return null;
    }

}
