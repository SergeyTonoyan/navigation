package ee.ria.datatransformationmodule.data.BaseStationReport;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "base_stations_reposts")
public class BaseStationReportEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private BaseStationEntity baseStationEntity;
    private UUID mobileStationId;
    private float distance;
    @Column(name = "time")
    private Timestamp timestamp;
}
