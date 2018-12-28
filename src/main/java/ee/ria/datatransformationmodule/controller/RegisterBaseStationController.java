package ee.ria.datatransformationmodule.controller;

import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationService;
import ee.ria.datatransformationmodule.dto.BaseStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(RegisterBaseStationController.API_ROUTE_MAPPING)
public class RegisterBaseStationController {

    public static final String MESSAGE_SUCCESS = "Base station has been registered";
    @Autowired
    BaseStationService baseStationService;

    public static final String API_ROUTE_MAPPING = "api/v1";

    @PostMapping("/register_bs")
    private ResponseEntity<String> registerBaseStation(@RequestBody @Valid BaseStation baseStation) {

        baseStationService.saveOrUpdate(baseStation);
        return new ResponseEntity<String>(MESSAGE_SUCCESS, HttpStatus.CREATED);
    }
}
