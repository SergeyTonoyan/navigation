package ee.ria.datatransformationmodule.dto;

import ee.ria.datatransformationmodule.data.ValidUuid;
import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseStation {

    @Id
    @ValidUuid
    @JsonProperty("id")
    private UUID id;

    @NotEmpty (message = "Please provide the name")
    @JsonProperty("name")
    private String name;

    @NotNull (message = "Please provide X coordinate")
    @JsonProperty("x")
    private Float x;

    @NotNull (message = "Please provide Y coordinate")
    @JsonProperty("y")
    private Float y;

    @NotNull(message = "Please provide detection radius")
    @Positive (message = "Detection radius value must be positive")
    @JsonProperty("detectionRadiusInMeters")
    private Float detectionRadiusInMeters;
}
