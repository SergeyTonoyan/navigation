package ee.ria.datatransformationmodule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ee.ria.datatransformationmodule.data.ValidUuid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class MobileStationData {

    @JsonProperty("mobile_station_id")
    @ValidUuid(message = "Please provide valid UUID")
    private UUID mobileStationId;

    @JsonProperty("distance")
    @NotNull(message = "Please provide valid distance")
    private Float distance;

    @JsonProperty("timestamp")
    @NotNull(message = "Please provide valid timestamp")
    private Timestamp timestamp;
}
