package com.quick.recording.gateway.config.cache;

import com.quick.recording.gateway.config.function.CacheConsumer;
import com.quick.recording.gateway.main.service.remote.CacheableMainRemoteServiceAbstract;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

@Configuration
@ConditionalOnProperty(value = "qr-cache.enabled", havingValue = "true")
public class CacheStreamConfigurer implements BeanDefinitionRegistryPostProcessor {


    @Override
    @SuppressWarnings("unchecked")
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(CacheableMainRemoteServiceAbstract.class));
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("com/quick/recording");
        for(BeanDefinition beanDefinition : candidateComponents){
            try {
                Class<? extends CacheableMainRemoteServiceAbstract<?>> clazz =
                        (Class<? extends CacheableMainRemoteServiceAbstract<?>>)
                                Class.forName(beanDefinition.getBeanClassName());
                Constructor<?> constructor = Arrays.stream(clazz.getConstructors())
                        .filter(item -> item.getParameterCount() == 2)
                        .findFirst()
                        .orElseThrow();
                Object object = constructor.newInstance(null,null);
                String cacheName = (String)clazz.getMethod("cacheName").invoke(object);
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CacheConsumer.class);
                registry.registerBeanDefinition(cacheName, builder.getBeanDefinition());
            }
            catch (Exception ignored){
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistryPostProcessor.super.postProcessBeanFactory(beanFactory);
    }
}
