package epruhlo.socialmultiplication.domain;

import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class Multiplication {

    private final int factorA;
    private final int factorB;

    public Multiplication() {
        this(0, 0);
    }
}
