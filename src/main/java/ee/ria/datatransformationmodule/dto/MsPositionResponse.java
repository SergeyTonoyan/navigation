package ee.ria.datatransformationmodule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MsPositionResponse {

    @JsonProperty("mobileID")
    public UUID mobileID;

    @JsonProperty("x")
    public float x;

    @JsonProperty("y")
    public float y;

    @JsonProperty("error_radius")
    public float errorRadius;

    @JsonProperty("error_code")
    public int errorCode;

    @JsonProperty("error_description")
    public String errorDescription;
}
