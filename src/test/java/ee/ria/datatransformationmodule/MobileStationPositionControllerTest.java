package ee.ria.datatransformationmodule;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.datatransformationmodule.controller.MobileStationPositionController;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportService;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MobileStationPositionController.class)
public class MobileStationPositionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BaseStationReportService reportService;
    private BaseStationEntity bsEntity1;
    private BaseStationEntity bsEntity2;
    private BaseStationEntity bsEntity3;
    private MobileStationData msData1;
    private MobileStationData msData2;
    private MobileStationData msData3;
    private BaseStationReportEntity bs1Report;
    private BaseStationReportEntity bs2Report;
    private BaseStationReportEntity bs3Report;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void initData() {

        bsEntity1 = new BaseStationEntity();
        bsEntity1.setId(UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d810"));
        bsEntity1.setName("bs1");
        bsEntity1.setX(5f);
        bsEntity1.setY(7f);
        bsEntity1.setDetectionRadiusInMeters(5f);

        bsEntity2 = new BaseStationEntity();
        bsEntity2.setId(UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d811"));
        bsEntity2.setName("bs2");
        bsEntity2.setX(9f);
        bsEntity2.setY(11f);
        bsEntity2.setDetectionRadiusInMeters(6f);

        bsEntity3 = new BaseStationEntity();
        bsEntity3.setId(UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d812"));
        bsEntity3.setName("bs3");
        bsEntity3.setX(12f);
        bsEntity3.setY(4f);
        bsEntity3.setDetectionRadiusInMeters(7f);

        UUID msId = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d820");
        Timestamp timestamp = Timestamp.valueOf("2018-01-12 13:00:00");

//        msData1 = new MobileStationData();
//        msData1.setMobileStationId(msId);
//        msData1.setDistance(4f);
//        msData1.setTimestamp(timestamp);

//        msData2 = new MobileStationData();
//        msData2.setMobileStationId(msId);
//        msData2.setDistance(4f);
//        msData2.setTimestamp(timestamp);
//
//        msData3 = new MobileStationData();
//        msData3.setMobileStationId(msId);
//        msData3.setDistance(4.24264f);
//        msData3.setTimestamp(timestamp);

        bs1Report = new BaseStationReportEntity();
        bs1Report.setBaseStationEntity(bsEntity1);
        bs1Report.setDistance(4f);
        bs1Report.setMobileStationId(msId);
        bs1Report.setTimestamp(timestamp);

        bs2Report = new BaseStationReportEntity();
        bs2Report.setBaseStationEntity(bsEntity2);
        bs2Report.setDistance(4f);
        bs2Report.setMobileStationId(msId);
        bs2Report.setTimestamp(timestamp);

        bs3Report = new BaseStationReportEntity();
        bs3Report.setBaseStationEntity(bsEntity3);
        bs3Report.setDistance(4.24264f);
        bs3Report.setMobileStationId(msId);
        bs3Report.setTimestamp(timestamp);



    }

    @Test
    public void shouldReturnResponseForInvalidIdIfIdIsNotValid() throws Exception {

        UUID invalidMsId = UUID.fromString("fc8c5dd3-0000-0000-0000-6160f830d800");

        mvc.perform(
                get("http://localhost:8080/api/v1/location/{id}",invalidMsId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error_code")
                        .value(10))
                .andExpect(jsonPath("$.error_description")
                        .value("ID is invalid. Please provide valid ID"));
    }

    @Test
    public void shouldReturnResponseMsNotFoundIfMsIdIsNotFound() throws Exception {

        UUID msId = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d811");
        when(reportService.getBsLatestReportsByMsId(msId)).thenReturn(null);

        mvc.perform(
                get("http://localhost:8080/api/v1/location/{id}",msId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error_code")
                        .value(15))
                .andExpect(jsonPath("$.error_description")
                        .value("Mobile station has never been detected by any base stations"));
    }

    @Test
    public void shouldReturnProperResponseIfMsRegisteredBy1Bs() throws Exception {

        UUID msId = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d820");
        List<BaseStationReportEntity> bsReports = new ArrayList<BaseStationReportEntity>();
        bsReports.add(bs1Report);
        when(reportService.getBsLatestReportsByMsId(msId)).thenReturn(bsReports);

        mvc.perform(
                get("http://localhost:8080/api/v1/location/{id}",msId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.x")
                        .value(5f))
                .andExpect(jsonPath("$.y")
                        .value(7f))
                .andExpect(jsonPath("$.error_radius")
                        .value(4f))
                .andExpect(jsonPath("$.error_code")
                        .value(0))
                .andExpect(jsonPath("$.error_description")
                        .value("No errors occurred"));
    }

    @Test
    public void shouldReturnProperResponseIfMsRegisteredBy2Bs() throws Exception {

        UUID msId = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d820");
        List<BaseStationReportEntity> bsReports = new ArrayList<BaseStationReportEntity>();
        bsReports.add(bs1Report);
        bsReports.add(bs2Report);
        when(reportService.getBsLatestReportsByMsId(msId)).thenReturn(bsReports);

        mvc.perform(
                get("http://localhost:8080/api/v1/location/{id}",msId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.x")
                        .value(7f))
                .andExpect(jsonPath("$.y")
                        .value(9f))
                .andExpect(jsonPath("$.error_radius")
                        .value(2.828427f))
                .andExpect(jsonPath("$.error_code")
                        .value(0))
                .andExpect(jsonPath("$.error_description")
                        .value("No errors occurred"));
    }

    @Test
    public void shouldReturnProperResponseIfMsRegisteredBy3Bs() throws Exception {

        UUID msId = UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d820");
        List<BaseStationReportEntity> bsReports = new ArrayList<BaseStationReportEntity>();
        bsReports.add(bs1Report);
        bsReports.add(bs2Report);
        bsReports.add(bs3Report);
        when(reportService.getBsLatestReportsByMsId(msId)).thenReturn(bsReports);

        mvc.perform(
                get("http://localhost:8080/api/v1/location/{id}",msId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.x")
                        .value(9f))
                .andExpect(jsonPath("$.y")
                        .value(7f))
                .andExpect(jsonPath("$.error_radius")
                        .value(0f))
                .andExpect(jsonPath("$.error_code")
                        .value(0))
                .andExpect(jsonPath("$.error_description")
                        .value("No errors occurred"));
    }
}
