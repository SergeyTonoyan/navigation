package ee.ria.datatransformationmodule.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PositionPoint {

    private float x;
    private float y;
    private float errorRadius;
}

