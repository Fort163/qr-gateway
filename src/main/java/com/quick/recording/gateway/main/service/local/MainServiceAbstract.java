package com.quick.recording.gateway.main.service.local;

import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.config.error.exeption.NotFoundException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.Delete;
import com.quick.recording.gateway.mapper.MainMapper;
import com.quick.recording.gateway.util.ReflectUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public abstract class MainServiceAbstract<Entity extends SmartEntity,
        Dto extends SmartDto,
        Repository extends JpaRepository<Entity, UUID>,
        Mapper extends MainMapper<Entity, Dto>>
        implements MainService<Entity, Dto> {

    protected final Repository repository;
    protected final Mapper mapper;
    protected final MessageUtil messageUtil;
    protected final Class<Entity> entityClass;

    public MainServiceAbstract() {
        throw new BuildClassException("Call empty constructor in class MainServiceAbstract");
    }

    protected MainServiceAbstract(Repository repository,
                                  Mapper mapper,
                                  MessageUtil messageUtil,
                                  Class<Entity> entityClass) {
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
        beforePost(entity);
        entity = repository.save(entity);
        afterPost(entity);
        return mapper.toDto(entity);
    }


    @Override
    @CircuitBreaker(name = "database")
    public Dto patch(Dto dto) {
        Assert.notNull(dto.getUuid(), "Uuid cannot be null");
        Entity entity = repository.findById(dto.getUuid()).orElseThrow(
                () -> new NotFoundException(messageUtil, entityClass, dto.getUuid())
        );
        beforePatch(entity);
        setNoEditableField(dto, entity);
        setPatchField(dto, entity);
        entity = mapper.toEntityWithoutNull(dto, entity);
        entity = repository.save(entity);
        afterPatch(entity);
        return mapper.toDto(entity);
    }


    @Override
    @CircuitBreaker(name = "database")
    public Dto put(Dto dto) {
        Assert.notNull(dto.getUuid(), "Uuid cannot be null");
        Entity entity = repository.findById(dto.getUuid()).orElseThrow(
                () -> new NotFoundException(messageUtil, entityClass, dto.getUuid())
        );
        beforePut(entity);
        setNoEditableField(dto, entity);
        entity = mapper.toEntity(dto, entity);
        entity = repository.save(entity);
        afterPut(entity);
        return mapper.toDto(repository.save(entity));
    }


    @Override
    @CircuitBreaker(name = "database")
    public Boolean delete(UUID uuid, Delete delete) {
        Assert.notNull(uuid, "Uuid cannot be null");
        return switch (delete) {
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
    public Boolean restore(UUID uuid) {
        Assert.notNull(uuid, "Uuid cannot be null");
        Entity entity = repository.findById(uuid).orElseThrow(
                () -> new NotFoundException(messageUtil, entityClass, uuid)
        );
        entity.setIsActive(true);
        return repository.save(entity).getIsActive();
    }

    @Override
    @CircuitBreaker(name = "database")
    public Entity save(Entity entity) {
        return repository.save(entity);
    }

    @Override
    @CircuitBreaker(name = "database")
    public List<Entity> saveAll(Collection<Entity> list) {
        return repository.saveAll(list);
    }

    @Override
    @CircuitBreaker(name = "database")
    public List<Entity> findAll() {
        return repository.findAll();
    }

    protected void beforePost(Entity entity) {

    }

    protected void afterPost(Entity entity) {

    }

    protected void beforePatch(Entity entity) {

    }

    protected void afterPatch(Entity entity) {

    }

    protected void beforePut(Entity entity) {

    }

    protected void afterPut(Entity entity) {

    }

    protected Example<Entity> createSearch(Entity entity) {
        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        prepareExampleMatcher(exampleMatcher);
        return Example.of(entity, exampleMatcher);
    }

    protected void setNoEditableField(Dto dto, Entity entity) {
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedWhen(entity.getCreatedWhen());
    }

    @SuppressWarnings("unchecked")
    private void setPatchField(Dto dto, Entity entity) {
        Class<? extends SmartDto> dtoClass = dto.getClass();
        Dto oldDto = getMapper().toDto(entity);
        List<Field> fields = ReflectUtil.arrayFieldFromClass(dtoClass);
        ReflectUtil.DataWorkerList dataWorkerListDto = ReflectUtil.getDataWorker(
                ReflectUtil.arrayFieldFromClass(dtoClass), dtoClass
        );
        try {
            for (Field field : fields) {
                ReflectUtil.DataWorker dataWorkerDto = dataWorkerListDto.getDataWorker(field.getName());
                if (Objects.nonNull(dataWorkerDto)) {
                    Object invokeList = dataWorkerDto.getGetter().invoke(dto);
                    if (Collection.class.isAssignableFrom(invokeList.getClass())) {
                        try {
                            Collection<? super BaseDto> oldCollection = (Collection<? super BaseDto>)
                                    dataWorkerDto.getGetter().invoke(oldDto);
                            Set<UUID> uuidSet = oldCollection.stream().map(item -> ((BaseDto) item).getUuid())
                                    .collect(Collectors.toSet());
                            Collection<? extends BaseDto> newCollection = (Collection<? extends BaseDto>) invokeList;
                            for (BaseDto next : newCollection) {
                                if (!uuidSet.contains(next.getUuid())) {
                                    oldCollection.add(next);
                                }
                            }
                            dataWorkerDto.getSetter().invoke(dto, oldCollection);
                        } catch (ClassCastException ignored) {
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException exception) {
            log.error(
                    String.format("Error in setPatchField for class : %s \nerror: %s", dtoClass.getName(),
                            exception.getMessage()));
        }
    }

    public abstract ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher);

    public Repository getRepository() {
        return repository;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public MessageUtil getMessageUtil() {
        return messageUtil;
    }

    public Class<Entity> getEntityClass() {
        return entityClass;
    }

}
