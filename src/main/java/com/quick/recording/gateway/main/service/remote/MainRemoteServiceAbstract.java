package com.quick.recording.gateway.main.service.remote;

import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.enumerated.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public abstract class MainRemoteServiceAbstract<Dto extends BaseDto> implements MainRemoteService<Dto>{

    protected MainRemoteService<Dto> service;

    public MainRemoteServiceAbstract() {
        throw new BuildClassException("Call empty constructor in class MainRemoteServiceAbstract");
    }

    public MainRemoteServiceAbstract(MainRemoteService<Dto> service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Dto> byUuid(UUID uuid) {
        return service.byUuid(uuid);
    }

    @Override
    public Page<Dto> search(Dto dto, Pageable pageable) {
        return service.search(dto,pageable);
    }

    @Override
    public ResponseEntity<Dto> post(Dto dto) {
        return service.post(dto);
    }

    @Override
    public ResponseEntity<Dto> patch(Dto dto) {
        return service.patch(dto);
    }

    @Override
    public ResponseEntity<Dto> put(Dto dto) {
        return service.put(dto);
    }

    @Override
    public ResponseEntity<Boolean> delete(UUID uuid, Delete delete) {
        return service.delete(uuid, delete);
    }
}
