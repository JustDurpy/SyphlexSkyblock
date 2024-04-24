package net.syphlex.skyblock.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pair<X, Y> {
    private X x;
    private Y y;

    public Pair(){}

    public Pair(X x, Y y){
        this.x = x;
        this.y = y;
    }
}
