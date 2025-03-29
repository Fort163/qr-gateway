package com.quick.recording.gateway.main.service.local;

import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.broker.MessageChangeDataDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.ChangeDataType;
import com.quick.recording.gateway.enumerated.Delete;
import com.quick.recording.gateway.mapper.MainMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeType;

import java.util.Objects;
import java.util.UUID;

public abstract class CacheableMainServiceAbstract <Entity extends SmartEntity, Dto extends BaseDto>
        extends MainServiceAbstract<Entity,Dto>{

    private String topicName;
    private StreamBridge streamBridge;

    public CacheableMainServiceAbstract() {
        throw new BuildClassException("Call empty constructor in class CacheableMainServiceAbstract");
    }

    protected CacheableMainServiceAbstract(JpaRepository<Entity, UUID> repository,
                                  MainMapper<Entity, Dto> mapper,
                                  MessageUtil messageUtil,
                                  Class<Entity> entityClass,
                                  StreamBridge streamBridge){
        super(repository, mapper, messageUtil, entityClass);
        if(!supportCacheable() && Objects.isNull(streamBridge)){
            throw new BuildClassException("This configuration not supported cacheable," +
                    " use MainServiceAbstract or configure broker link and change Dto with RedisHash annotation");
        }
        this.streamBridge = streamBridge;
        this.topicName = createCacheTopicName();
    }

    @Override
    public Dto post(Dto dto) {
        Dto result = super.post(dto);
        convertAndSend(result.getUuid(),ChangeDataType.CREATE);
        return result;
    }

    @Override
    public Dto patch(Dto dto) {
        Dto result = super.patch(dto);
        convertAndSend(result.getUuid(),ChangeDataType.UPDATE);
        return result;
    }

    @Override
    public Dto put(Dto dto) {
        Dto result = super.put(dto);
        convertAndSend(result.getUuid(),ChangeDataType.UPDATE);
        return result;
    }

    @Override
    public Boolean delete(UUID uuid, Delete delete) {
        Boolean result = super.delete(uuid, delete);
        convertAndSend(uuid,ChangeDataType.DELETE);
        return result;
    }

    @Override
    public Entity save(Entity entity) {
        Entity result = super.save(entity);
        convertAndSend(result.getUuid(),ChangeDataType.CREATE);
        return result;
    }

    private void convertAndSend(UUID uuid, ChangeDataType type){
        streamBridge.send(topicName, createMessage(uuid, type), MimeType.valueOf("application/json"));
    }

    private Message<MessageChangeDataDto> createMessage(UUID uuid, ChangeDataType type){
        MessageChangeDataDto messageChangeDataDto = new MessageChangeDataDto();
        messageChangeDataDto.setEntityUUID(uuid);
        messageChangeDataDto.setType(type);
        return MessageBuilder.withPayload(messageChangeDataDto).build();
    }

    private Boolean supportCacheable(){
        return Objects.nonNull(cacheName());
    }

    private String createCacheTopicName(){
        return String.format("%s-in-0",cacheName());
    }

}
