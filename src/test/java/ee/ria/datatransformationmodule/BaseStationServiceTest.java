package ee.ria.datatransformationmodule;

import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationRepository;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class BaseStationServiceTest {

    @TestConfiguration
    static class BaseStationServiceTestContextConfiguration {

        @Bean
        public BaseStationService baseStationService() {
            return new BaseStationService();
        }
    }

    @Autowired
    private BaseStationService baseStationService;

    @MockBean
    private BaseStationRepository baseStationRepository;

    @Before
    public void setup() {

        BaseStationEntity bs = new BaseStationEntity();
        UUID id = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d814");
        bs.setId(id);
        Mockito.when(baseStationRepository.findById(bs.getId())).thenReturn(java.util.Optional.of(bs));
    }

    @Test
    public void whenValidId_thenBsShouldBeFound() {

        UUID id = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d814");
        BaseStationEntity found = baseStationService.findById(id);
        assertThat(found.getId()).isEqualTo(id);
    }
}

