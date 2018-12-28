package ee.ria.datatransformationmodule.data.BaseStationReport;

import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface BaseStationReportsRepository extends CrudRepository<BaseStationReportEntity, UUID> {

    @Query("SELECT MAX(bsReport.timestamp) FROM BaseStationReportEntity bsReport WHERE bsReport.mobileStationId = ?1")
    public Timestamp findLatestTimeStampByMsID(UUID msId);

    @Query("SELECT bsReport FROM BaseStationReportEntity bsReport WHERE (bsReport.mobileStationId = ?1) AND " +
            "((bsReport.timestamp >= ?2) AND (bsReport.timestamp <= ?3))")
    public List<BaseStationReportEntity> findReportsByMsIdAndTimeInterval(UUID msID, Timestamp startTime,
                                                                          Timestamp endTime);
}
