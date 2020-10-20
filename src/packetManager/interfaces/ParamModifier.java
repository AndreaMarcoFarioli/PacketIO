package packetManager.interfaces;

@FunctionalInterface
public interface ParamModifier<T> {
    T modify(T param);
}