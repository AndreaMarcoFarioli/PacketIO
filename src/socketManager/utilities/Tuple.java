package socketManager.utilities;

/**
 * An ordered and immutable set of two values.
 * @param <K> - first value type
 * @param <V> - second value type
 */
public class Tuple<K, V> {
    private final K firstValue;
    private final V secondValue;
    public Tuple(K firstValue, V secondValue){
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public K getFirstValue() {
        return firstValue;
    }

    public V getSecondValue() {
        return secondValue;
    }

    public boolean equals(Tuple<K, V> object){
        return firstValue.equals(object.firstValue) && secondValue.equals(object.secondValue);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "firstValue=" + firstValue +
                ", secondValue=" + secondValue +
                '}';
    }
}
