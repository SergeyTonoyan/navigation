package ee.ria.datatransformationmodule.controller;

import ee.ria.datatransformationmodule.util.MsPositionCalculator;
import ee.ria.datatransformationmodule.util.MsResponseBuilder;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportEntity;
import ee.ria.datatransformationmodule.data.BaseStationReport.BaseStationReportService;
import ee.ria.datatransformationmodule.data.BaseStation.BaseStationService;
import ee.ria.datatransformationmodule.dto.MsPositionResponse;
import ee.ria.datatransformationmodule.data.PositionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(MobileStationPositionController.API_ROUTE_MAPPING)
public class MobileStationPositionController {

    @Autowired
    BaseStationReportService baseStationReportService;

    public static final String API_ROUTE_MAPPING = "api/v1";

    @GetMapping("/location/{id}")
    @ResponseBody
    private MsPositionResponse getMobileStationLocation(@PathVariable("id") String stringId) {

        MsPositionResponse positionResponse = null;
        if (!isIdValid(stringId)) return positionResponse = MsResponseBuilder.getResponseForInvalidId();

        UUID id = UUID.fromString(stringId);
        List<BaseStationReportEntity> bsReports = baseStationReportService.getBsLatestReportsByMsId(id);

        if ((bsReports == null) || (bsReports.size() == 0)) return positionResponse =
                MsResponseBuilder.getResponseIfMsNotFound(id);

        PositionPoint msPosition = null;
        int numberOfBsReported = bsReports.size();

        if (numberOfBsReported == 1) {

            msPosition = new PositionPoint(bsReports.get(0).getBaseStationEntity().getX(),
                    bsReports.get(0).getBaseStationEntity().getY(), bsReports.get(0).getDistance());
        } else {

            float bs1x = bsReports.get(0).getBaseStationEntity().getX();
            float bs1y = bsReports.get(0).getBaseStationEntity().getY();
            float bs1Dist = bsReports.get(0).getDistance();

            float bs2x = bsReports.get(1).getBaseStationEntity().getX();
            float bs2y = bsReports.get(1).getBaseStationEntity().getY();
            float bs2Dist = bsReports.get(1).getDistance();

            if (numberOfBsReported == 2)
                msPosition = MsPositionCalculator.getPositionBy2Bs(bs1x, bs1y, bs1Dist, bs2x, bs2y, bs2Dist);

            if (numberOfBsReported > 2) {
                float bs3x = bsReports.get(2).getBaseStationEntity().getX();
                float bs3y = bsReports.get(2).getBaseStationEntity().getY();
                float bs3Dist = bsReports.get(2).getDistance();

                msPosition = MsPositionCalculator.getPositionBy3Bs(bs1x, bs1y, bs1Dist, bs2x, bs2y, bs2Dist, bs3x,
                        bs3y, bs3Dist);
            }
        }
        return positionResponse = MsResponseBuilder.getResponseIfMsDetected(msPosition, id);
    }

    private boolean isIdValid (String uuid) {

        String regex = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";
        return uuid != null && uuid.matches(regex);
    }
}
