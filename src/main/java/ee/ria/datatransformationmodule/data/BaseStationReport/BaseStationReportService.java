package ee.ria.datatransformationmodule.data.BaseStationReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class BaseStationReportService {

   @Autowired
   BaseStationReportsRepository baseStationReportsRepository;
   private static final int DELTA_TIME_SECS = 1; //assuming that bs might report ms position with 1 sec delay


   public void save(BaseStationReportEntity baseStationReportEntity){

       baseStationReportsRepository.save(baseStationReportEntity);
   }

   public List<BaseStationReportEntity> getBsLatestReportsByMsId(UUID msId) {

       Timestamp latestTimestamp = baseStationReportsRepository.findLatestTimeStampByMsID(msId);
       if (latestTimestamp == null) return null;
       Timestamp startTime = addTimeSec(latestTimestamp, DELTA_TIME_SECS);
       List<BaseStationReportEntity> bsReports = baseStationReportsRepository.findReportsByMsIdAndTimeInterval(msId,
               startTime, latestTimestamp);
       return bsReports;
   }

   private Timestamp addTimeSec(Timestamp timestamp, int timeSec) {

       Calendar cal = Calendar.getInstance();
       cal.setTimeInMillis(timestamp.getTime());
       cal.add(Calendar.SECOND, - timeSec);
       return new Timestamp(cal.getTime().getTime());
   }
}
