package com.quick.recording.gateway.config.advice;

import com.quick.recording.gateway.annotations.RemoteFill;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.main.controller.MainControllerAbstract;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import com.quick.recording.gateway.util.ContextUtil;
import com.quick.recording.gateway.util.ReflectUtil;
import com.quick.recording.gateway.util.ReflectUtil.DataWorker;
import com.quick.recording.gateway.util.ReflectUtil.DataWorkerList;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@RestControllerAdvice
@Log4j2
public class FillRemoteAdvice implements ResponseBodyAdvice<Object> {

    private final ApplicationContext context;

    @Autowired
    public FillRemoteAdvice(ApplicationContext context) {
        this.context = context;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            Class aClass = null;
            if(MainControllerAbstract.class.isAssignableFrom(returnType.getContainingClass())){
                aClass = ((MainControllerAbstract)getContext().getBean(returnType.getContainingClass())).getType();
            }
            else {
                Type type = returnType.getGenericParameterType();
                aClass = getClass(type);
            }
            return isSupport(aClass);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return false;
        }

    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            Class aClass = null;
            if(MainControllerAbstract.class.isAssignableFrom(returnType.getContainingClass())){
                aClass = ((MainControllerAbstract)getContext().getBean(returnType.getContainingClass())).getType();
            }
            else {
                Type type = returnType.getGenericParameterType();
                aClass = getClass(type);
            }
            if (body instanceof Page) {
                upgrade((Page) body, aClass);
            }
            if (body instanceof HttpEntity) {
                upgrade(((HttpEntity<?>) body).getBody(), aClass);
            }
            if (Collection.class.isAssignableFrom(body.getClass())) {
                final Class finalClass = aClass;
                ((Collection<?>)body).forEach(item -> upgrade(item, finalClass));
            }
            if (aClass.isAssignableFrom(body.getClass())) {
                upgrade(body, aClass);
            }
            return body;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return body;
        }
    }

    private void upgrade(Page<?> body, Class<?> aClass) throws InvocationTargetException, IllegalAccessException {
        for (Object object : body.getContent()) {
            if (aClass.isAssignableFrom(object.getClass())) {
                upgrade(object, aClass);
            }
        }
    }

    private void upgrade(Object object, Class<?> aClass) {

        List<Field> uuidFields = ReflectUtil.classTypeFieldFromClass(aClass, UUID.class);
        List<Field> remoteFields = ReflectUtil.annotatedFieldFromClass(aClass, RemoteFill.class);
        DataWorkerList uuidDataWorker = ReflectUtil.getDataWorker(uuidFields, aClass);
        DataWorkerList remoteDataWorker = ReflectUtil.getDataWorker(remoteFields, aClass);
        for (DataWorker dataWorker : remoteDataWorker) {
            try {
                RemoteFill annotation = dataWorker.getField().getAnnotation(RemoteFill.class);
                DataWorker dataWorkerUUID = uuidDataWorker.getDataWorker(annotation.fieldName());
                MainRemoteService<? extends SmartDto> service = ContextUtil.findService(getContext(), annotation.typeDto());
                Object invokeUUID = dataWorkerUUID.getGetter().invoke(object);
                if (invokeUUID instanceof UUID) {
                    ResponseEntity<? extends SmartDto> responseEntity = service.byUuid((UUID) invokeUUID);
                    dataWorker.getSetter().invoke(object, responseEntity.getBody());
                }
            } catch (Exception ignored) {
            }
        }
    }


    @SuppressWarnings("rawtypes")
    private boolean isSupport(Class type) {
        return Arrays.stream((type).getDeclaredFields())
                .anyMatch(item -> Objects.nonNull(item.getAnnotation(RemoteFill.class)));
    }

    @SuppressWarnings("rawtypes")
    private Class getClass(Type type) {
        if (type instanceof ParameterizedType) {
            return getClass((ParameterizedType) type);
        }
        if (type instanceof Class) {
            return (Class) type;
        }
        throw new RuntimeException("Class not found");
    }

    @SuppressWarnings("rawtypes")
    private Class getClass(ParameterizedType type) {
        return Arrays.stream(type.getActualTypeArguments())
                .filter(item -> item instanceof Class)
                .map(item -> (Class) item).findFirst().orElseThrow();
    }

    public ApplicationContext getContext() {
        return context;
    }

}
