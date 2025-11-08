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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
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
        Entity entity = getRepository().findById(uuid).orElseThrow(
                () -> new NotFoundException(getMessageUtil(), getEntityClass(), uuid)
        );
        return getMapper().toDto(entity);
    }

    @Override
    @CircuitBreaker(name = "database")
    public Page<Dto> list(Dto dto, Pageable pageable) {
        Entity entity = getMapper().toEntity(dto);
        Page<Dto> page = getRepository().findAll(createSearch(entity), pageable).map(getMapper()::toDto);
        return page;
    }

    @Override
    @CircuitBreaker(name = "database")
    @Transactional
    public Dto post(Dto dto) {
        Entity entity = getMapper().toEntity(dto);
        this.beforePost(entity, dto);
        entity = getRepository().saveAndFlush(entity);
        this.afterPost(entity, dto);
        return getMapper().toDto(entity);
    }


    @Override
    @CircuitBreaker(name = "database")
    @Transactional
    public Dto patch(Dto dto) {
        Assert.notNull(dto.getUuid(), "Uuid cannot be null");
        Entity entity = getRepository().findById(dto.getUuid()).orElseThrow(
                () -> new NotFoundException(getMessageUtil(), getEntityClass(), dto.getUuid())
        );
        Dto oldDto = getMapper().toDto(entity);
        this.beforePatch(entity, oldDto, dto);
        this.setNoEditableField(dto, entity);
        this.setPatchField(dto, entity);
        entity = getMapper().toEntityWithoutNull(dto, entity);
        entity = getRepository().save(entity);
        Dto result = getMapper().toDto(entity);
        this.afterPatch(oldDto, result);
        return result;
    }


    @Override
    @CircuitBreaker(name = "database")
    @Transactional
    public Dto put(Dto dto) {
        Assert.notNull(dto.getUuid(), "Uuid cannot be null");
        Entity entity = getRepository().findById(dto.getUuid()).orElseThrow(
                () -> new NotFoundException(getMessageUtil(), getEntityClass(), dto.getUuid())
        );
        Dto oldDto = getMapper().toDto(entity);
        this.beforePut(entity, oldDto, dto);
        this.setNoEditableField(dto, entity);
        entity = getMapper().toEntity(dto, entity);
        entity = getRepository().save(entity);
        Dto result = getMapper().toDto(entity);
        this.afterPut(oldDto, result);
        return result;
    }


    @Override
    @CircuitBreaker(name = "database")
    @Transactional
    public Dto delete(UUID uuid, Delete delete) {
        Assert.notNull(uuid, "Uuid cannot be null");
        Entity entity = getRepository().findById(uuid).orElseThrow(
                () -> new NotFoundException(getMessageUtil(), getEntityClass(), uuid)
        );
        this.beforeDelete(entity, delete);
        return switch (delete) {
            case HARD -> {
                this.getRepository().delete(entity);
                this.afterDelete(entity, delete);
                yield this.getMapper().toDto(entity);
            }
            case SOFT -> {
                entity.setIsActive(false);
                entity = getRepository().save(entity);
                this.afterDelete(entity, delete);
                yield this.getMapper().toDto(entity);
            }
        };
    }

    @Override
    @CircuitBreaker(name = "database")
    @Transactional
    public Dto restore(UUID uuid) {
        Assert.notNull(uuid, "Uuid cannot be null");
        Entity entity = getRepository().findById(uuid).orElseThrow(
                () -> new NotFoundException(getMessageUtil(), getEntityClass(), uuid)
        );
        this.beforeRestore(entity);
        entity.setIsActive(true);
        entity = getRepository().save(entity);
        this.afterRestore(entity);
        return this.getMapper().toDto(entity);
    }

    @Override
    @CircuitBreaker(name = "database")
    public Entity save(Entity entity) {
        return getRepository().save(entity);
    }

    @Override
    @CircuitBreaker(name = "database")
    public List<Entity> saveAll(Collection<Entity> list) {
        return getRepository().saveAll(list);
    }

    @Override
    @CircuitBreaker(name = "database")
    public void deleteAll(Collection<UUID> ids) {
        getRepository().deleteAllById(ids);
    }

    @Override
    @CircuitBreaker(name = "database")
    public void deleteAllBatch(Collection<UUID> ids) {
        getRepository().deleteAllByIdInBatch(ids);
    }

    @Override
    @CircuitBreaker(name = "database")
    public Entity byUuidEntity(UUID uuid) {
        return getRepository().findById(uuid).orElseThrow(
                () -> new NotFoundException(getMessageUtil(), getEntityClass(), uuid)
        );
    }

    @Override
    @CircuitBreaker(name = "database")
    public List<Entity> findAll() {
        return repository.findAll();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void beforePost(Entity entity, Dto dto) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void afterPost(Entity entity, Dto dto) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void beforePatch(Entity entity, Dto oldDto, Dto dto) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void afterPatch(Dto oldDto, Dto newDto) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void beforePut(Entity entity, Dto oldDto, Dto dto) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void afterPut(Dto oldDto, Dto newDto) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void beforeDelete(Entity entity, Delete delete) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void afterDelete(Entity entity, Delete delete) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void beforeRestore(Entity entity) {

    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void afterRestore(Entity entity) {

    }

    protected Example<Entity> createSearch(Entity entity) {
        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        exampleMatcher = prepareExampleMatcher(exampleMatcher);
        return Example.of(entity, exampleMatcher);
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

    private BaseDto clearValueDto(Object valueDto) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = valueDto.getClass().getConstructor();
        BaseDto result = (BaseDto) constructor.newInstance();
        result.setUuid(((BaseDto) valueDto).getUuid());
        return result;
    }

    @SuppressWarnings("unchecked")
    protected void undoEditChild(Dto dto, Entity entity) {
        Class<? extends SmartDto> dtoClass = dto.getClass();
        List<Field> fieldsSingleDto = ReflectUtil.classTypeFieldFromClass(dtoClass, BaseDto.class);
        List<Field> fieldsArray = ReflectUtil.arrayFieldFromClass(dtoClass);
        ReflectUtil.DataWorkerList dataWorkerSingle = ReflectUtil.getDataWorker(fieldsSingleDto, dtoClass);
        ReflectUtil.DataWorkerList dataWorkerArray = ReflectUtil.getDataWorker(fieldsArray, dtoClass);
        try {
            for (Field field : fieldsSingleDto) {
                ReflectUtil.DataWorker dataWorker = dataWorkerSingle.getDataWorker(field.getName());
                if (Objects.nonNull(dataWorker)) {
                    Object valueDto = dataWorker.getGetter().invoke(dto);
                    if (Objects.isNull(valueDto)) {
                        continue;
                    }
                    if (BaseDto.class.isAssignableFrom(valueDto.getClass())) {
                        dataWorker.getSetter().invoke(dto, clearValueDto(valueDto));
                    }
                }
            }
            try {
                for (Field field : fieldsArray) {
                    ReflectUtil.DataWorker dataWorker = dataWorkerArray.getDataWorker(field.getName());
                    Collection<? super BaseDto> collection = (Collection<? super BaseDto>) dataWorker.getGetter().invoke(dto);
                    if (Objects.isNull(collection)) {
                        continue;
                    }
                    Collection<? super BaseDto> result = new ArrayList<>();
                    for (Object baseDto : collection) {
                        result.add(clearValueDto(baseDto));
                    }
                    dataWorker.getSetter().invoke(dto, result);
                }
            } catch (ClassCastException ignored) {
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException exception) {
            log.error(
                    String.format("Error in undoEditChild for class : %s \nerror: %s", dtoClass.getName(),
                            exception.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void setNoEditableField(Dto dto, Entity entity) {
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedWhen(entity.getCreatedWhen());
        undoEditChild(dto, entity);
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
                    Object oldInvokeList = dataWorkerDto.getGetter().invoke(oldDto);
                    if (Objects.isNull(invokeList)) {
                        invokeList = new ArrayList<>();
                    }
                    if (Objects.isNull(oldInvokeList)) {
                        oldInvokeList = new ArrayList<>();
                    }
                    if (Collection.class.isAssignableFrom(invokeList.getClass())) {
                        Collection<? extends Object> invokeListObject = (Collection<? extends Object>) invokeList;
                        Collection<? super Object> oldInvokeListObject = (Collection<? super Object>) oldInvokeList;
                        if(!invokeListObject.isEmpty() || !oldInvokeListObject.isEmpty()){
                            Object nextObject;
                            if(invokeListObject.iterator().hasNext()) {
                                nextObject = invokeListObject.iterator().next();
                            }
                            else {
                                nextObject = oldInvokeListObject.iterator().next();
                            }
                            if(BaseDto.class.isAssignableFrom(nextObject.getClass())){
                                try {
                                    Collection<? super BaseDto> oldCollection = (Collection<? super BaseDto>) oldInvokeListObject;
                                    Collection<? extends BaseDto> newCollection = (Collection<? extends BaseDto>) invokeListObject;
                                    Set<UUID> uuidSet = oldCollection.stream().map(item -> ((BaseDto) item).getUuid())
                                            .collect(Collectors.toSet());
                                    for (BaseDto next : newCollection) {
                                        if (!uuidSet.contains(next.getUuid())) {
                                            oldCollection.add(next);
                                        }
                                    }
                                    dataWorkerDto.getSetter().invoke(dto, oldCollection);
                                } catch (Exception ignored) {
                                }
                            }
                            else {
                                try {
                                    for (Object newObject : invokeListObject){
                                        if(!oldInvokeListObject.contains(newObject)){
                                            oldInvokeListObject.add(newObject);
                                        }
                                    }
                                    dataWorkerDto.getSetter().invoke(dto, oldInvokeListObject);
                                }
                                catch (Exception ignored) {
                                }
                            }
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

}
