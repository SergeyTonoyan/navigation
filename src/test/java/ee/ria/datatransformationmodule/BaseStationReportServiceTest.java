package ee.ria.datatransformationmodule;

import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationRepository;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationService;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportService;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class BaseStationReportServiceTest {

    @TestConfiguration
    static class BaseStationReportServiceTestContextConfiguration {

        @Bean
        public BaseStationReportService baseStationReportService() {
            return new BaseStationReportService();
        }
    }

    @Autowired
    private BaseStationReportService reportService;

    @MockBean
    private BaseStationReportsRepository reportsRepository;

    @Before
    public void setup() {

        BaseStationReportEntity bsReport = new BaseStationReportEntity();
        UUID id = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d819");
        bsReport.setMobileStationId(id);
        bsReport.setTimestamp(Timestamp.valueOf("2018-01-12 13:00:00"));
        Mockito.when(reportsRepository.findLatestTimeStampByMsID(bsReport.getMobileStationId())).
                thenReturn(bsReport.getTimestamp());
        List<BaseStationReportEntity> bsReports = new ArrayList<BaseStationReportEntity>();
        bsReports.add(bsReport);
        Mockito.when(reportsRepository.findReportsByMsIdAndTimeInterval(bsReport.getMobileStationId(),
                Timestamp.valueOf("2018-01-12 12:59:59"), bsReport.getTimestamp())).thenReturn(bsReports);
    }

    @Test
    public void shouldReturnBsReportsListWithLatestTimeStampByValidId() {

        UUID id = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d819");
        List<BaseStationReportEntity> found = reportService.getBsLatestReportsByMsId(id);
        assertThat(found.get(0).getMobileStationId()).isEqualTo(id);
    }
}

