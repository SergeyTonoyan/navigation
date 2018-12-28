package ee.ria.datatransformationmodule;

import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BaseStationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    BaseStationRepository baseStationRepository;

    @Test
    public void writeToDbAndFindByID() {

        final String uuidString = "fc8c5dd3-d46c-4945-894d-6160f830d818";
        final UUID id = UUID.fromString(uuidString);
        BaseStationEntity baseStationEntity = new BaseStationEntity();
        baseStationEntity.setId(id);
        baseStationEntity.setName("bs1");
        baseStationEntity.setX(2f);
        baseStationEntity.setY(3f);
        baseStationEntity.setDetectionRadiusInMeters(4f);

        entityManager.persist(baseStationEntity);
        entityManager.flush();

        BaseStationEntity found = baseStationRepository.findById(id).get();
        assertThat(found.getName()).isEqualTo(baseStationEntity.getName());
    }

}
