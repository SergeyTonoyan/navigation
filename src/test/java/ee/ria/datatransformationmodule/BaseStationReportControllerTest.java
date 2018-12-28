package ee.ria.datatransformationmodule;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.datatransformationmodule.controller.BaseStationReportController;
import ee.ria.datatransformationmodule.controller.RegisterBaseStationController;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationService;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportService;
import ee.ria.datatransformationmodule.dto.BaseStation;
import ee.ria.datatransformationmodule.dto.BaseStationMessage;
import ee.ria.datatransformationmodule.dto.MobileStationData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BaseStationReportController.class)
public class BaseStationReportControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BaseStationReportService reportService;

    @MockBean
    private BaseStationService bsService;

    private BaseStationEntity bsEntity;
    private MobileStationData msData;
    private BaseStationMessage bsMessage;
    private BaseStationReportEntity bsReportEntity;


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void initData() {

        UUID bsId = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d814");
        UUID msId = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d800");

        bsEntity = new BaseStationEntity(bsId, "bs2", 3f, 2f,6f);

        msData = new MobileStationData();
        msData.setMobileStationId(msId);
        msData.setTimestamp(Timestamp.valueOf("2018-01-12 13:00:00"));
        msData.setDistance(5f);

        List<MobileStationData> msDataList = new ArrayList<MobileStationData>();
        msDataList.add(msData);

        bsMessage = new BaseStationMessage();
        bsMessage.setBsId(bsId);
        bsMessage.setMobileStationDataList(msDataList);

        bsReportEntity = new BaseStationReportEntity();
        bsReportEntity.setBaseStationEntity(bsEntity);
        bsReportEntity.setMobileStationId(msId);
        bsReportEntity.setDistance(msData.getDistance());
        bsReportEntity.setTimestamp(msData.getTimestamp());

    }

    @Test
    public void shouldReturnSuccessMessageIfBsInfoIsCorrect() throws Exception {

        when(bsService.findById(bsMessage.getBsId())).thenReturn(bsEntity);
        doNothing().when(reportService).save(bsReportEntity);

        mvc.perform(
                post("http://localhost:8080/api/v1/report")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(asJsonString(bsMessage))
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().string(BaseStationReportController.INFORMATION_HAS_BEEN_RECEIVED));
    }

    @Test
    public void shouldReturnBsNotRegisteredMessageIfBsIdNotFoundInDb() throws Exception {

        when(bsService.findById(bsMessage.getBsId())).thenThrow(new NoSuchElementException());
        doNothing().when(reportService).save(bsReportEntity);

        mvc.perform(
                post("http://localhost:8080/api/v1/report")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(bsMessage))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().string(BaseStationReportController.NOT_REGISTERED_IN_THE_SYSTEM));
    }
}
