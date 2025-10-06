package com.quick.recording.gateway.main.controller;

import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.config.function.CacheConsumer;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.broker.MessageChangeDataDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Log4j2
public abstract class CacheableMainControllerAbstract<Dto extends BaseDto,
        Entity extends SmartEntity,
        Repository extends JpaRepository<Dto, UUID>,
        Service extends MainService<Entity, Dto>>
        extends MainControllerAbstract<Dto, Entity, Service> {

    private final Repository repository;
    @Autowired
    private ApplicationContext context;

    public CacheableMainControllerAbstract() {
        throw new BuildClassException("Call empty constructor in class CacheableMainControllerAbstract");
    }

    public CacheableMainControllerAbstract(Service service,
                                           Repository repository) {
        super(service);
        this.repository = repository;
    }

    @PostConstruct
    private void postConstruct() {
        for (String beanName : Objects.requireNonNull(listAllCacheName())) {
            try {
                CacheConsumer bean = (CacheConsumer) getContext().getBean(beanName);
                this.addConsumer(bean);
            } catch (BeansException | NullPointerException exception) {
                throw new BuildClassException(String.format("Bean with name << %s >> not found.\n" +
                                "Check if you have enabled the option qr-cache.enabled.\n" +
                                "Or change the abstract class CacheableMainControllerAbstract to MainControllerAbstract.",
                        beanName));
            }
        }

    }

    @Override
    public ResponseEntity<Dto> byUuid(UUID uuid) {
        Optional<Dto> byId = getRepository().findById(uuid);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get());
        } else {
            ResponseEntity<Dto> dtoResponseEntity = super.byUuid(uuid);
            getRepository().save(dtoResponseEntity.getBody());
            return dtoResponseEntity;
        }
    }

    private void addConsumer(CacheConsumer consumer) {
        consumer.addFunction(this::consumeMessage);
    }

    private void consumeMessage(MessageChangeDataDto dto) {
        try {
            switch (dto.getType()) {
                case CREATE -> {
                    this.doCreatedMessage(dto);
                    break;
                }
                case UPDATE -> {
                    this.doUpdatedMessage(dto);
                    break;
                }
                case DELETE -> {
                    this.doDeletedMessage(dto);
                    break;
                }
            }
        } catch (Exception exception) {
            log.error("Error while processing message : " + dto);
        }
    }

    protected void doCreatedMessage(MessageChangeDataDto dto) {

    }

    protected void doUpdatedMessage(MessageChangeDataDto dto) {
        defaultDoMessage(dto);
    }

    protected void doDeletedMessage(MessageChangeDataDto dto) {
        defaultDoMessage(dto);
    }

    private void defaultDoMessage(MessageChangeDataDto dto) {
        if (dto.getClazz().equals(getType())) {
            if (getRepository().existsById(dto.getEntityUUID())) {
                getRepository().deleteById(dto.getEntityUUID());
            }
        } else {
            List<Dto> searchDto = createSearchDto(dto);
            for (Dto search : searchDto) {
                List<Dto> all = this.repository.findAll(createSearch(search));
                getRepository().deleteAll(all);
            }
        }
    }

    public Repository getRepository() {
        return repository;
    }

    public ApplicationContext getContext() {
        return context;
    }

    private Example<Dto> createSearch(Dto dto) {
        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues();
        return Example.of(dto, exampleMatcher);
    }

    private List<Dto> createSearchDto(MessageChangeDataDto message) {
        List<Dto> result = new ArrayList<>();
        try {
            List<Map.Entry<String, Class<? extends BaseDto>>> entries = mapClassToFieldName().entrySet().stream()
                    .filter(entry -> entry.getValue().getName().equals(message.getClazz().getName()))
                    .toList();
            for (Map.Entry<String, Class<? extends BaseDto>> entry : entries) {
                Constructor<? extends BaseDto> constructorDto = entry.getValue().getDeclaredConstructor();
                BaseDto baseDto = constructorDto.newInstance();
                baseDto.setUuid(message.getEntityUUID());
                Constructor<Dto> constructor = getType().getDeclaredConstructor();
                Field field = getType().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                Dto dto = constructor.newInstance();
                if (Collection.class.isAssignableFrom(field.getType())) {
                    field.set(dto, List.of(baseDto));
                } else {
                    field.set(dto, baseDto);
                }
                result.add(dto);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                IllegalAccessException | NoSuchFieldException e) {
            log.error("Failed to process message : " + message);
        }
        return result;
    }


}
