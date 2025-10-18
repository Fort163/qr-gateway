package com.quick.recording.gateway.main.controller;

import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.util.Patch;
import com.quick.recording.gateway.dto.util.Post;
import com.quick.recording.gateway.dto.util.Put;
import com.quick.recording.gateway.enumerated.Delete;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public interface MainController<Dto extends SmartDto> {

    String SELF = "self";

    @GetMapping({"/{uuid}"})
    ResponseEntity<Dto> byUuid(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid);

    @GetMapping
    Page<Dto> list(@SpringQueryMap Dto search, Pageable pageable);

    @PostMapping
    ResponseEntity<Dto> post(@RequestBody @Validated({Post.class}) Dto dto);

    @PutMapping({"/patch"})
    ResponseEntity<Dto> patch(@RequestBody @Validated({Patch.class}) Dto dto);

    @PutMapping
    ResponseEntity<Dto> put(@RequestBody @Validated({Put.class}) Dto dto);

    @DeleteMapping({"/{uuid}"})
    ResponseEntity<Dto> delete(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid,
                                   @RequestParam(name = "delete")
                                   @NotNull(message = "{validation.description}") Delete delete);

    @PutMapping({"/{uuid}"})
    ResponseEntity<Dto> restore(@PathVariable @NotNull(message = "{validation.uuid}") UUID uuid);

    Class<Dto> getType();

    @Nullable
    default Collection<String> listAllCacheName() {
        boolean supportCache = getType().isAnnotationPresent(RedisHash.class);
        if (supportCache) {
            return mapClassToFieldName().entrySet().stream().map(entry -> cacheName(entry.getValue())).toList();
        }
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    default Map<String, Class<? extends BaseDto>> mapClassToFieldName() {
        boolean supportCache = getType().isAnnotationPresent(RedisHash.class);
        if (supportCache) {
            Map<String, Class<? extends BaseDto>> result = new HashMap<>();
            result.put(SELF, getType());
            Map<String, Type> annotatedTypes = Arrays.stream(getType().getDeclaredFields())
                    .collect(Collectors.toMap(Field::getName, field -> field.getAnnotatedType().getType()));
            result.putAll((annotatedTypes.entrySet().stream()
                    .filter(entry -> entry.getValue() instanceof Class)
                    .filter(entry -> ((Class<? extends BaseDto>) entry.getValue()).isAnnotationPresent(RedisHash.class))
                    .map(entry -> Map.entry(entry.getKey(), (Class<? extends BaseDto>) entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
            result.putAll((annotatedTypes.entrySet().stream()
                    .filter(entry -> entry.getValue() instanceof ParameterizedType)
                    .filter(entry -> ((ParameterizedType) entry.getValue()).getActualTypeArguments().length == 1)
                    .map(entry -> Map.entry(Arrays.stream(((ParameterizedType) entry.getValue()).getActualTypeArguments())
                            .findFirst(), entry.getKey()))
                    .filter(entry -> entry.getKey().isPresent() && entry.getKey().get() instanceof Class)
                    .filter(entry -> ((Class<? extends BaseDto>) entry.getKey().get()).isAnnotationPresent(RedisHash.class))
                    .map(entry -> Map.entry(entry.getValue(), (Class<? extends BaseDto>) entry.getKey().get()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
            return result;
        }
        return null;
    }

    default String cacheName(Class<?> clazz) {
        return clazz.getAnnotation(RedisHash.class).value().toLowerCase(Locale.ROOT);
    }

}
