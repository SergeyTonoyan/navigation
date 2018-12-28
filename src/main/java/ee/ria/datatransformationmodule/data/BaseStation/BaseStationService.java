package ee.ria.datatransformationmodule.data.BaseStation;

import ee.ria.datatransformationmodule.dto.BaseStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BaseStationService {

    @Autowired
    BaseStationRepository baseStationRepository;

    public void saveOrUpdate(BaseStation baseStation) {

        BaseStationEntity baseStationEntity = new BaseStationEntity();
        baseStationEntity.setId(baseStation.getId());
        baseStationEntity.setName(baseStation.getName());
        baseStationEntity.setX(baseStation.getX());
        baseStationEntity.setY(baseStation.getY());
        baseStationEntity.setDetectionRadiusInMeters(baseStation.getDetectionRadiusInMeters());
        baseStationRepository.save(baseStationEntity);
    }

    public BaseStationEntity findById(UUID id) {
        return baseStationRepository.findById(id).get();
    }
}
