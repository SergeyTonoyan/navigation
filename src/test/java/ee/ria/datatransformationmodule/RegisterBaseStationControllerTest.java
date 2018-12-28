package ee.ria.datatransformationmodule;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.datatransformationmodule.controller.RegisterBaseStationController;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationService;
import ee.ria.datatransformationmodule.dto.BaseStation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegisterBaseStationController.class)
public class RegisterBaseStationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BaseStationService service;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturnSuccessMessageIfBsInfoIsCorrect() throws Exception {

        BaseStation bs = new BaseStation(UUID.fromString("fc8c5dd3-d46c-4945-894d-6160f830d814"),
                "bs2", 3f, 2f,6f);

        doNothing().when(service).saveOrUpdate(bs);

        mvc.perform(
                post("http://localhost:8080/api/v1/register_bs")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(asJsonString(bs))
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().string(RegisterBaseStationController.MESSAGE_SUCCESS));
    }

    @Test
    public void shouldReturnBadRequestIfIdIsNotValid() throws Exception {

        BaseStation bs = new BaseStation(UUID.fromString("fc8c5dd3-d46c-4945-094d-6160f830d814"),
                "bs2", 3f, 2f,6f);
        doNothing().when(service).saveOrUpdate(bs);

        mvc.perform(
                post("http://localhost:8080/api/v1/register_bs")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(bs))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}
