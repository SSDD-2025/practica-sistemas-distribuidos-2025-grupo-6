package es.dlj.onlinestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UtilMethods {

    public static <T> List<Map<String, Object>> getMappedList(Collection<T> items, T selectedValue) {
        List<Map<String, Object>> mappedList = new ArrayList<>();
        for (T item : items) {
            mappedList.add(Map.of(
                "name", item.toString(),
                "selected", (selectedValue != null && selectedValue.equals(item))
            ));
        }
        return mappedList;
    }

    public static <T> List<Map<String, Object>> getMappedList(Class<T> enumClass, T selectedValue) {
        return getMappedList(List.of(enumClass.getEnumConstants()), selectedValue);
    }
}
