package ee.ria.datatransformationmodule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ee.ria.datatransformationmodule.data.ValidUuid;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class BaseStationMessage {

    @JsonProperty ("base_station_id")
    @ValidUuid(message = "Please provide valid UUID")
    private UUID bsId;

    @JsonProperty ("reports")
    @JsonDeserialize(as = List.class, contentAs = MobileStationData.class)
    @Embedded
    @Valid
    private List<MobileStationData> mobileStationDataList;
}
