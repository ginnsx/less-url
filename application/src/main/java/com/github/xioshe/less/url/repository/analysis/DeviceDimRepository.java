package com.github.xioshe.less.url.repository.analysis;

import com.github.xioshe.less.url.entity.analysis.DeviceDim;
import com.github.xioshe.less.url.repository.BaseRepository;
import com.github.xioshe.less.url.mapper.DeviceDimMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceDimRepository extends BaseRepository<DeviceDimMapper, DeviceDim> {

    public Long getOrCreateDeviceId(String device, String brand, String deviceType) {
        DeviceDim deviceDim = this.lambdaQuery()
                .eq(DeviceDim::getDevice, device)
                .eq(DeviceDim::getBrand, brand)
                .eq(DeviceDim::getDeviceType, deviceType)
                .one();

        if (deviceDim == null) {
            deviceDim = new DeviceDim();
            deviceDim.setDevice(device);
            deviceDim.setBrand(brand);
            deviceDim.setDeviceType(deviceType);
            this.save(deviceDim);
        }

        return deviceDim.getId();
    }
}