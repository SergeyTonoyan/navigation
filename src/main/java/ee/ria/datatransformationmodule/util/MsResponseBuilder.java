package ee.ria.datatransformationmodule.util;

import ee.ria.datatransformationmodule.data.PositionPoint;
import ee.ria.datatransformationmodule.dto.MsPositionResponse;

import java.util.UUID;

public class MsResponseBuilder {

    private static final String ERROR_ID_IS_INVALID = "ID is invalid. Please provide valid ID";
    private static final String ERROR_MS_NOT_FOUND = "Mobile station has never been detected by any base stations";
    private static final String ERROR_OK = "No errors occurred";

    private static final int ERROR_CODE_ID_IS_INVALID = 10;
    private static final int ERROR_CODE_MS_NOT_FOUND = 15;
    private static final int ERROR_CODE_OK = 0;

    public static MsPositionResponse getResponseForInvalidId() {

        MsPositionResponse positionResponse = new MsPositionResponse();
        positionResponse.setErrorCode(ERROR_CODE_ID_IS_INVALID);
        positionResponse.setErrorDescription(ERROR_ID_IS_INVALID);
        return positionResponse;
    }

    public static MsPositionResponse getResponseIfMsNotFound(UUID msID) {

        MsPositionResponse positionResponse = new MsPositionResponse();
        positionResponse.setMobileID(msID);
        positionResponse.setErrorCode(ERROR_CODE_MS_NOT_FOUND);
        positionResponse.setErrorDescription(ERROR_MS_NOT_FOUND);
        return positionResponse;
    }

    public static MsPositionResponse getResponseIfMsDetected(PositionPoint msPosition, UUID msID) {

        MsPositionResponse positionResponse = new MsPositionResponse();
        positionResponse.setMobileID(msID);
        positionResponse.setX(msPosition.getX());
        positionResponse.setY(msPosition.getY());
        positionResponse.setErrorRadius(msPosition.getErrorRadius());
        positionResponse.setErrorCode(ERROR_CODE_OK);
        positionResponse.setErrorDescription(ERROR_OK);
        return positionResponse;
    }
}
