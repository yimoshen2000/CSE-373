package problems;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - Do not add any additional imports
 * - Do not create new `int[]` objects for `toString` or `rotateRight`
 */
public class ArrayProblems {

    /**
     * Returns a `String` representation of the input array.
     * Always starts with '[' and ends with ']'; elements are separated by ',' and a space.
     */
    public static String toString(int[] array) {
        String result = "[";
        if (array.length > 0) {
            result += array[0];
            for (int i = 1; i < array.length; i++) {
                result += ", " + array[i];
            }
        }
        result += "]";
        return result;
    }

    /**
     * Returns a new array containing the input array's elements in reversed order.
     * Does not modify the input array.
     */
    public static int[] reverse(int[] array) {
        int[] reversedResult = new int[array.length];
        int n = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            n++;
            reversedResult[n] = array[i];
        }
        return reversedResult;
    }

    /**
     * Rotates the values in the array to the right.
     */
    public static void rotateRight(int[] array) {
        if (array.length > 1) {
            int lastElement = array[array.length - 1];
            for (int i = array.length - 1; i >= 1; i--) {
                array[i] = array[i - 1];
            }
            array[0] = lastElement;
        }
    }
}
