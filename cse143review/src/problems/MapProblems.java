package problems;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * See the spec on the website for example behavior.
 */
public class MapProblems {

    /**
     * Returns true if any string appears at least 3 times in the given list; false otherwise.
     */
    public static boolean contains3(List<String> list) {
        Map<String, Integer> counts = new HashMap<String, Integer>();

        for (int i = 0; i < list.size(); i++) {
            if (!counts.containsKey(list.get(i))) {
                counts.put(list.get(i), 0);
            }
            counts.put(list.get(i), counts.get(list.get(i)) + 1);
        }

        for (int count : counts.values()) {
            if (count >= 3) {
                return true;
            }
        }
        return false;

    }

    /**
     * Returns a map containing the intersection of the two input maps.
     * A key-value pair exists in the output iff the same key-value pair exists in both input maps.
     */
    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        for (String key : m1.keySet()) {
            if (m2.containsKey(key) && m1.get(key) == m2.get(key)) {
                resultMap.put(key, m1.get(key));
            }
        }

        return resultMap;
    }
}
