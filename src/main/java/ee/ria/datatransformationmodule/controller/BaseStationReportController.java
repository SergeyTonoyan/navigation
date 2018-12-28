package ee.ria.datatransformationmodule.controller;

import ee.ria.datatransformationmodule.data.BaseStation.BaseStationEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportService;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationService;
import ee.ria.datatransformationmodule.dto.BaseStationMessage;
import ee.ria.datatransformationmodule.dto.MobileStationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(RegisterBaseStationController.API_ROUTE_MAPPING)
public class BaseStationReportController {

    public static final String NOT_REGISTERED_IN_THE_SYSTEM = "BS with such ID is not registered in the system";
    public static final String INFORMATION_HAS_BEEN_RECEIVED = "Information from BS has been received";

    @Autowired
    BaseStationReportService baseStationReportService;

    @Autowired
    BaseStationService baseStationService;

    @PostMapping("/report")
    private ResponseEntity<String> reportMobileStation(@RequestBody @Valid
                                                                           BaseStationMessage baseStationMessage) {
        try {
            BaseStationEntity baseStationEntity = baseStationService.findById(baseStationMessage.getBsId());
            for (MobileStationData msData : baseStationMessage.getMobileStationDataList()) {

                BaseStationReportEntity temp = new BaseStationReportEntity();
                temp.setBaseStationEntity(baseStationEntity);
                temp.setMobileStationId(msData.getMobileStationId());
                temp.setDistance(msData.getDistance());
                temp.setTimestamp(msData.getTimestamp());
                baseStationReportService.save(temp);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>(NOT_REGISTERED_IN_THE_SYSTEM, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(INFORMATION_HAS_BEEN_RECEIVED, HttpStatus.CREATED);
    }
}
