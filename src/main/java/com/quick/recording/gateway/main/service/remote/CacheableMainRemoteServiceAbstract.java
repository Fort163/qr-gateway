package com.quick.recording.gateway.main.service.remote;

import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.config.function.CacheConsumer;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.broker.MessageChangeDataDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public abstract class CacheableMainRemoteServiceAbstract<Dto extends BaseDto> extends MainRemoteServiceAbstract<Dto> {

    private CrudRepository<Dto, UUID> repository;
    private CacheConsumer consumer;
    @Autowired
    private ApplicationContext context;

    public CacheableMainRemoteServiceAbstract() {
        throw new BuildClassException("Call empty constructor in class CacheableMainRemoteServiceAbstract");
    }

    public CacheableMainRemoteServiceAbstract(CrudRepository<Dto, UUID> repository,
                                              MainRemoteService<Dto> service) {
        super(service);
        this.repository = repository;
    }

    @PostConstruct
    private void postConstruct() {
        try {
            CacheConsumer bean = (CacheConsumer) context.getBean(cacheName());
            this.setConsumer(bean);
        }
        catch (BeansException | NullPointerException exception){
            throw new BuildClassException(String.format("Bean with name << %s >> not found.\n" +
                    "Check if you have enabled the option qr-cache.enabled.\n" +
                    "Or change the abstract class CacheableMainRemoteServiceAbstract to MainRemoteServiceAbstract.", cacheName()));
        }
    }

    @Override
    public ResponseEntity<Dto> byUuid(UUID uuid) {
        Optional<Dto> byId = this.repository.findById(uuid);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get());
        } else {
            ResponseEntity<Dto> dtoResponseEntity = super.byUuid(uuid);
            repository.save(dtoResponseEntity.getBody());
            return dtoResponseEntity;
        }
    }

    public void setConsumer(CacheConsumer consumer) {
        this.consumer = consumer;
        this.consumer.setFunction(this::consumeMessage);
    }

    private void consumeMessage(MessageChangeDataDto dto) {
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
    }

    protected void doCreatedMessage(MessageChangeDataDto dto) {

    }

    protected void doUpdatedMessage(MessageChangeDataDto dto) {
        if (this.repository.existsById(dto.getEntityUUID())) {
            this.repository.deleteById(dto.getEntityUUID());
        }
    }

    protected void doDeletedMessage(MessageChangeDataDto dto) {
        if (this.repository.existsById(dto.getEntityUUID())) {
            this.repository.deleteById(dto.getEntityUUID());
        }
    }

}
