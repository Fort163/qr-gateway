package com.quick.recording.gateway.service.company;

import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "GATEWAY-SERVICE",
        contextId = "geocoder",
        path = "/company/api/v1/geocoder")
public interface CompanyServiceGeocoderApi extends MainRemoteService<GeocoderDto> {

    @Override
    default Class<GeocoderDto> getType(){
        return GeocoderDto.class;
    }

}
