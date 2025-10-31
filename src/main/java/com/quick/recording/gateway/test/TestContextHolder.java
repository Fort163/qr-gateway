package com.quick.recording.gateway.test;

import com.quick.recording.gateway.dto.BaseDto;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TestContextHolder implements BeforeEachCallback {

    private final HashMap<String, LinkedList<? super BaseDto>> currentMap;

    private HashMap<String, ? super BaseDto> lastDeletedElement;

    private String IS_SUITE = "IS_SUITE";

    public TestContextHolder() {
        this.currentMap = new HashMap<>();
        this.lastDeletedElement = new HashMap<>();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        System.setProperty(IS_SUITE, String.valueOf(context.getUniqueId().contains("suite:")));
    }

    public boolean put(String key, BaseDto value) {
        if(this.currentMap.containsKey(key)){
            int indexByUUID = findIndexByUUID(this.currentMap.get(key), value.getUuid());
            if(indexByUUID > -1){
                this.currentMap.get(key).remove(indexByUUID);
                this.currentMap.get(key).add(indexByUUID, value);
                return true;
            }
            else {
                return this.currentMap.get(key).add(value);
            }
        }
        else {
            LinkedList<BaseDto> list = new LinkedList<>();
            list.add(value);
            return Objects.nonNull(this.currentMap.put(key, list));
        }
    }

    public boolean remove(String key, BaseDto value) {
        if(this.currentMap.containsKey(key)){
            int indexByUUID = findIndexByUUID(this.currentMap.get(key), value.getUuid());
            if(indexByUUID > -1){
                BaseDto remove = (BaseDto) this.currentMap.get(key).remove(indexByUUID);
                this.lastDeletedElement.put(key, remove);
                return true;
            }
        }
        return false;
    }

    @Nullable
    public BaseDto getLastDeleted(String key){
        return (BaseDto) this.lastDeletedElement.get(key);
    }

    @Nullable
    public BaseDto getByNumber(String key, int number){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)){
            return (BaseDto) this.currentMap.get(key).get(number);
        }
        return null;
    }

    @Nullable
    public BaseDto getByUUID(String key, UUID uuid){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)) {
            int indexByUUID = findIndexByUUID(this.currentMap.get(key), uuid);
            if (indexByUUID > -1) {
                return (BaseDto) this.currentMap.get(key).get(indexByUUID);
            }
        }
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends BaseDto> T getLast(String key){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)){
            try {
                return (T) this.currentMap.get(key).getLast();
            }
            catch (NoSuchElementException ignored){
            }
        }
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends BaseDto> List<T> getList(String key){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)){
            return this.currentMap.get(key).stream().map(item -> (T) item).collect(Collectors.toList());
        }
        return null;
    }

    public int findIndexByUUID(List<? super BaseDto> list, UUID uuid) {
        OptionalInt index = IntStream.range(0, list.size())
                .filter(i -> ((BaseDto) list.get(i)).getUuid().equals(uuid))
                .findFirst();
        return index.isPresent() ? index.getAsInt() : -1;
    }

    public boolean isSuite(){
        return Boolean.parseBoolean(System.getProperty(IS_SUITE));
    }

}
