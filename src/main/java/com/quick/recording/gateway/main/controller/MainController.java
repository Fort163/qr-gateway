package com.quick.recording.gateway.main.controller;

import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.enumerated.Delete;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface MainController<Dto> {

    @GetMapping({"/{uuid}"})
    ResponseEntity<Dto> byUuid(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid);

    @GetMapping
    Page<Dto> search(@SpringQueryMap Dto search, Pageable pageable);

    @PostMapping
    ResponseEntity<Dto> post(@RequestBody @Validated({Post.class}) Dto dto);

    @PutMapping({"/patch"})
    ResponseEntity<Dto> patch(@RequestBody @Validated({Patch.class}) Dto dto);

    @PutMapping
    ResponseEntity<Dto> put(@RequestBody @Validated({Put.class}) Dto dto);

    @DeleteMapping({"/{uuid}"})
    ResponseEntity<Boolean> delete(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid,
                                   @RequestParam(name = "delete")
                                   @NotNull(message = "{validation.description}") Delete delete);

}
