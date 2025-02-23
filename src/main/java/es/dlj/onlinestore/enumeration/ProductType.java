package es.dlj.onlinestore.enumeration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum ProductType {
    NEW,
    RECONDITIONED,
    SECONDHAND;

    public static List<Map<String, Object>> getMapped(ProductType type) {
        List<Map<String, Object>> productTypes = new ArrayList<>();
        for (ProductType pType : ProductType.values()) {
            productTypes.add(Map.of("name", pType.toString(), "selected", (type != null && type.equals(pType))));
        }
        return productTypes;
    }

    public static List<Map<String, Object>> getMapped() {
        return getMapped(NEW);
    }
}
