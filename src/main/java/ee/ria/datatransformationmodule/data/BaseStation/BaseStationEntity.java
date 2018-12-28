package ee.ria.datatransformationmodule.data.BaseStation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "base_stations")
public class BaseStationEntity {

    @Id
    @Column(name = "bs_id")
    private UUID id;
    private String name;
    private Float x;
    private Float y;
    private Float detectionRadiusInMeters;
}
