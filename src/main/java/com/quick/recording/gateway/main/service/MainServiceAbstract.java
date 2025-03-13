package com.quick.recording.gateway.main.service;

import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.config.error.exeption.NotFoundException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.Delete;
import com.quick.recording.gateway.mapper.MainMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

import java.util.UUID;

public abstract class MainServiceAbstract <Entity extends SmartEntity, Dto extends BaseDto>
        implements MainService<Entity, Dto>{

    protected final JpaRepository<Entity, UUID> repository;
    protected final MainMapper<Entity, Dto> mapper;
    protected final MessageUtil messageUtil;
    protected final Class<Entity> entityClass;

    public MainServiceAbstract() {
        throw new BuildClassException("Call empty constructor in class MainServiceAbstract");
    }

    protected MainServiceAbstract(JpaRepository<Entity, UUID> repository,
                                  MainMapper<Entity, Dto> mapper,
                                  MessageUtil messageUtil,
                                  Class<Entity> entityClass){
        this.repository = repository;
        this.mapper = mapper;
        this.messageUtil = messageUtil;
        this.entityClass = entityClass;
    }


    @Override
    @CircuitBreaker(name = "database")
    public Dto byUuid(UUID uuid) {
        Assert.notNull(uuid, "Uuid cannot be null");
        Entity entity = repository.findById(uuid).orElseThrow(
                () -> new NotFoundException(messageUtil, entityClass, uuid)
        );
        return mapper.toDto(entity);
    }

    @Override
    @CircuitBreaker(name = "database")
    public Page<Dto> search(Dto dto, Pageable pageable) {
        Entity entity = mapper.toEntity(dto);
        Page<Dto> page = repository.findAll(createSearch(entity), pageable).map(mapper::toDto);
        return page;
    }

    @Override
    @CircuitBreaker(name = "database")
    public Dto post(Dto dto) {
        Entity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }


    @Override
    @CircuitBreaker(name = "database")
    public Dto patch(Dto dto) {
        Assert.notNull(dto.getUuid(), "Uuid cannot be null");
        Entity entity = repository.findById(dto.getUuid()).orElseThrow(
                () -> new NotFoundException(messageUtil, entityClass, dto.getUuid())
        );
        entity = mapper.toEntityWithoutNull(dto, entity);
        return mapper.toDto(repository.save(entity));
    }


    @Override
    @CircuitBreaker(name = "database")
    public Dto put(Dto dto) {
        Assert.notNull(dto.getUuid(), "Uuid cannot be null");
        Entity entity = repository.findById(dto.getUuid()).orElseThrow(
                () -> new NotFoundException(messageUtil, entityClass, dto.getUuid())
        );
        entity = mapper.toEntity(dto, entity);
        return mapper.toDto(repository.save(entity));
    }


    @Override
    @CircuitBreaker(name = "database")
    public Boolean delete(UUID uuid, Delete delete) {
        Assert.notNull(uuid, "Uuid cannot be null");
        return switch (delete){
            case HARD -> {
                repository.findById(uuid).ifPresent(repository::delete);
                yield true;
            }
            case SOFT -> {
                Entity entity = repository.findById(uuid).orElseThrow(
                        () -> new NotFoundException(messageUtil, entityClass, uuid)
                );
                entity.setIsActive(false);
                yield !repository.save(entity).getIsActive();
            }
        };
    }

    @Override
    @CircuitBreaker(name = "database")
    public Entity save(Entity entity) {
        return repository.save(entity);
    }

    protected Example<Entity> createSearch(Entity entity){
        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        prepareExampleMatcher(exampleMatcher);
        return Example.of(entity,exampleMatcher);
    }

    public abstract ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher);

}
