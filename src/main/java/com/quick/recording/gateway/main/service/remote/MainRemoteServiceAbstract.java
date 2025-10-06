package com.quick.recording.gateway.main.service.remote;

import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.enumerated.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public abstract class MainRemoteServiceAbstract<Dto extends BaseDto, Service extends MainRemoteService<Dto>>
        implements MainRemoteService<Dto> {

    protected Service service;

    public MainRemoteServiceAbstract() {
        throw new BuildClassException("Call empty constructor in class MainRemoteServiceAbstract");
    }

    public MainRemoteServiceAbstract(Service service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Dto> byUuid(UUID uuid) {
        return getService().byUuid(uuid);
    }

    @Override
    public Page<Dto> search(Dto dto, Pageable pageable) {
        return getService().search(dto, pageable);
    }

    @Override
    public ResponseEntity<Dto> post(Dto dto) {
        return getService().post(dto);
    }

    @Override
    public ResponseEntity<Dto> patch(Dto dto) {
        return getService().patch(dto);
    }

    @Override
    public ResponseEntity<Dto> put(Dto dto) {
        return getService().put(dto);
    }

    @Override
    public ResponseEntity<Boolean> delete(UUID uuid, Delete delete) {
        return getService().delete(uuid, delete);
    }

    @Override
    public ResponseEntity<Boolean> restore(UUID uuid) {
        return getService().restore(uuid);
    }

    public Service getService() {
        return service;
    }

}
