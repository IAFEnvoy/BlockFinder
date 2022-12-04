package iafenvoy.blockfinder.util;

public class TripleGroup<R, S, T> {
    public final R first;
    public final S second;
    public final T third;

    private TripleGroup(R first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <R, S, T> TripleGroup<R, S, T> of(R first, S second, T third) {
        return new TripleGroup<>(first, second, third);
    }
}
