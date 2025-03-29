package com.quick.recording.gateway.main.controller;

import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.Delete;
import com.quick.recording.gateway.main.service.local.MainService;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public abstract class MainControllerAbstract<Dto extends BaseDto,
        Entity extends SmartEntity, Service extends MainService<Entity,Dto>> implements MainController<Dto>{

    protected final Service service;

    public MainControllerAbstract() {
        throw new BuildClassException("Call empty constructor in class MainControllerAbstract");
    }

    public MainControllerAbstract(Service service) {
        this.service = service;
    }

    @Override
    @GetMapping({"/{uuid}"})
    @PreAuthorize("hasAnyAuthority(#root.this.byUuidAuthority())")
    public ResponseEntity<Dto> byUuid(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid) {
        return ResponseEntity.ok(service.byUuid(uuid));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyAuthority(#root.this.searchAuthority())")
    public Page<Dto> search(@SpringQueryMap Dto search, Pageable pageable) {
        return service.search(search,pageable);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyAuthority(#root.this.postAuthority())")
    public ResponseEntity<Dto> post(@RequestBody @Validated({Post.class}) Dto dto) {
        return ResponseEntity.ok(service.post(dto));
    }

    @Override
    @PutMapping({"/patch"})
    @PreAuthorize("hasAnyAuthority(#root.this.patchAuthority())")
    public ResponseEntity<Dto> patch(@RequestBody @Validated({Patch.class}) Dto dto) {
        return ResponseEntity.ok(service.patch(dto));
    }

    @Override
    @PutMapping
    @PreAuthorize("hasAnyAuthority(#root.this.putAuthority())")
    public ResponseEntity<Dto> put(@RequestBody @Validated({Put.class}) Dto dto) {
        return ResponseEntity.ok(service.put(dto));
    }

    @Override
    @DeleteMapping({"/{uuid}"})
    @PreAuthorize("hasAnyAuthority(#root.this.deleteAuthority())")
    public ResponseEntity<Boolean> delete(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid,
                                          @RequestParam(name = "delete")
                                          @NotNull(message = "{validation.description}") Delete delete) {
        return ResponseEntity.ok(service.delete(uuid,delete));
    }

    /*
         Override these methods if you need
         different authorization settings.
     */

    public String byUuidAuthority() {
        return "ROLE_READ";
    }

    public String searchAuthority() {
        return "ROLE_READ";
    }

    public String postAuthority() {
        return "ROLE_WRITE";
    }
    public String patchAuthority() {
        return "ROLE_WRITE";
    }
    public String putAuthority() {
        return "ROLE_WRITE";
    }
    public String deleteAuthority() {
        return "ROLE_WRITE";
    }

}
