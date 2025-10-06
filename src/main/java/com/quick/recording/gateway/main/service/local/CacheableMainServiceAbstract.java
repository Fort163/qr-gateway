package com.quick.recording.gateway.main.service.local;

import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.config.error.exeption.BuildClassException;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.broker.MessageChangeDataDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.ChangeDataType;
import com.quick.recording.gateway.enumerated.Delete;
import com.quick.recording.gateway.mapper.MainMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeType;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
public abstract class CacheableMainServiceAbstract<Entity extends SmartEntity,
        Dto extends SmartDto,
        Repository extends JpaRepository<Entity, UUID>,
        Mapper extends MainMapper<Entity, Dto>>
        extends MainServiceAbstract<Entity, Dto, Repository, Mapper> {

    private final String topicName;
    private final StreamBridge streamBridge;

    public CacheableMainServiceAbstract() {
        throw new BuildClassException("Call empty constructor in class CacheableMainServiceAbstract");
    }

    protected CacheableMainServiceAbstract(Repository repository,
                                           Mapper mapper,
                                           MessageUtil messageUtil,
                                           Class<Entity> entityClass,
                                           StreamBridge streamBridge) {
        super(repository, mapper, messageUtil, entityClass);
        if (!supportCacheable() && Objects.isNull(streamBridge)) {
            throw new BuildClassException("This configuration not supported cacheable," +
                    " use MainServiceAbstract or configure broker link and change Dto with RedisHash annotation");
        }
        this.streamBridge = streamBridge;
        this.topicName = createCacheTopicName();
    }

    @Override
    public Dto post(Dto dto) {
        Dto result = super.post(dto);
        convertAndSend(result.getUuid(), ChangeDataType.CREATE);
        return result;
    }

    @Override
    public Dto patch(Dto dto) {
        Dto result = super.patch(dto);
        convertAndSend(result.getUuid(), ChangeDataType.UPDATE);
        return result;
    }

    @Override
    public Dto put(Dto dto) {
        Dto result = super.put(dto);
        convertAndSend(result.getUuid(), ChangeDataType.UPDATE);
        return result;
    }

    @Override
    public Boolean delete(UUID uuid, Delete delete) {
        Boolean result = super.delete(uuid, delete);
        convertAndSend(uuid, ChangeDataType.DELETE);
        return result;
    }

    @Override
    public Boolean restore(UUID uuid) {
        Boolean result = super.restore(uuid);
        convertAndSend(uuid, ChangeDataType.UPDATE);
        return result;
    }

    @Override
    public Entity save(Entity entity) {
        Entity result = super.save(entity);
        convertAndSend(result.getUuid(), ChangeDataType.CREATE);
        return result;
    }

    @Override
    public List<Entity> saveAll(Collection<Entity> list) {
        List<Entity> result = super.saveAll(list);
        result.forEach(item -> convertAndSend(item.getUuid(), ChangeDataType.CREATE));
        return result;
    }

    private void convertAndSend(UUID uuid, ChangeDataType type) {
        Message<MessageChangeDataDto> message = createMessage(uuid, type);
        try {
            streamBridge.send(topicName, message, MimeType.valueOf("application/json"));
        } catch (Exception exception) {
            log.error("Message not send : " + message);
        }
    }

    private Message<MessageChangeDataDto> createMessage(UUID uuid, ChangeDataType type) {
        MessageChangeDataDto messageChangeDataDto = new MessageChangeDataDto();
        messageChangeDataDto.setEntityUUID(uuid);
        messageChangeDataDto.setType(type);
        messageChangeDataDto.setClazz(getType());
        return MessageBuilder.withPayload(messageChangeDataDto).build();
    }

    private Boolean supportCacheable() {
        return Objects.nonNull(cacheName());
    }

    private String createCacheTopicName() {
        return String.format("%s-out-0", cacheName());
    }

    public String getTopicName() {
        return topicName;
    }

    public StreamBridge getStreamBridge() {
        return streamBridge;
    }
}
