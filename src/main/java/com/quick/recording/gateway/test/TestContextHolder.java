package com.quick.recording.gateway.test;

import com.quick.recording.gateway.dto.BaseDto;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Profile({"test"})
public class TestContextHolder<T extends BaseDto> implements BeforeEachCallback {

    private T lastCreateObject;

    private T lastDeletedObject;

    private String IS_SUITE = "IS_SUITE";

    public TestContextHolder() {
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        System.setProperty(IS_SUITE, String.valueOf(context.getUniqueId().contains("suite:")));
    }

    public boolean isSuite(){
        return Boolean.parseBoolean(System.getProperty(IS_SUITE));
    }

    public T getLastCreateObject() {
        return lastCreateObject;
    }

    public void setLastCreateObject(T lastCreateObject) {
        this.lastCreateObject = lastCreateObject;
    }

    public T getLastDeletedObject() {
        return lastDeletedObject;
    }

    public void setLastDeletedObject(T lastDeletedObject) {
        this.lastDeletedObject = lastDeletedObject;
    }
}
