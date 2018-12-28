package ee.ria.datatransformationmodule;

import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationRepository;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportsRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BaseStationReportsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    BaseStationReportsRepository reportsRepository;

    private BaseStationEntity baseStationEntity;

    @Before
    public void setupBaseStationEnity() {

        final String uuidString = "fc8c5dd3-d46c-4945-894d-6160f830d817";
        final UUID id = UUID.fromString(uuidString);
        BaseStationEntity baseStationEntity = new BaseStationEntity();
        baseStationEntity.setId(id);
        baseStationEntity.setName("bs6");
        baseStationEntity.setX(7f);
        baseStationEntity.setY(30f);
        baseStationEntity.setDetectionRadiusInMeters(9f);

        entityManager.persist(baseStationEntity);
        entityManager.flush();
    }

    @Test
    public void shouldFindLatestTimeStampByMsId() {

        BaseStationReportEntity reportEntity1 = new BaseStationReportEntity();
        reportEntity1.setBaseStationEntity(baseStationEntity);
        reportEntity1.setMobileStationId(UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d810"));
        reportEntity1.setDistance(4f);
        reportEntity1.setTimestamp(Timestamp.valueOf("2018-01-01 13:00:00"));

        entityManager.persist(reportEntity1);
        entityManager.flush();

        String latestTimestampString = "2018-01-02 13:00:00";

        BaseStationReportEntity reportEntity2 = new BaseStationReportEntity();
        reportEntity2.setBaseStationEntity(baseStationEntity);
        reportEntity2.setMobileStationId(UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d810"));
        reportEntity2.setDistance(4f);
        reportEntity2.setTimestamp(Timestamp.valueOf(latestTimestampString));

        entityManager.persist(reportEntity2);
        entityManager.flush();

        Timestamp foundTimestamp = reportsRepository.findLatestTimeStampByMsID(reportEntity1.getMobileStationId());
        assertThat(foundTimestamp.getTime() == Timestamp.valueOf(latestTimestampString).getTime());
    }

    @Test
    public void shouldFindReportsByMsIdAndTimestampInterval() {

        String[] timestampStrings = {

                "2018-01-02 13:00:00",
                "2018-01-03 13:00:00",
                "2018-01-04 13:00:00",
                "2018-01-10 15:00:00",
                "2018-01-12 13:00:00"
        };

        String[] msIDs = {

                "fc8c5dd3-d46c-4945-894d-6160f830d814",
                "fc8c5dd3-d46c-4945-894d-6160f830d811",
                "fc8c5dd3-d46c-4945-894d-6160f830d812",
                "fc8c5dd3-d46c-4945-894d-6160f830d813",
                "fc8c5dd3-d46c-4945-894d-6160f830d814"
        };

        for (int i = 0; i < timestampStrings.length; i++) {

            BaseStationReportEntity reportEntity = new BaseStationReportEntity();
            reportEntity.setBaseStationEntity(baseStationEntity);
            reportEntity.setMobileStationId(UUID.fromString(msIDs[i]));
            reportEntity.setDistance(4f);
            reportEntity.setTimestamp(Timestamp.valueOf(timestampStrings[i]));
            entityManager.persist(reportEntity);
            entityManager.flush();
        }
        List<BaseStationReportEntity> found = reportsRepository.findReportsByMsIdAndTimeInterval(UUID.fromString(msIDs[0]),
                Timestamp.valueOf(timestampStrings[3]), Timestamp.valueOf(timestampStrings[4]));
        assertThat(found.get(0).getMobileStationId().toString().equals(msIDs[0]));
    }
}
