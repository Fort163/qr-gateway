package com.quick.recording.gateway.config.cache;

import com.quick.recording.gateway.config.function.CacheConsumer;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.main.controller.MainController;
import com.quick.recording.gateway.main.service.remote.CacheableMainRemoteServiceAbstract;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Constructor;
import java.util.*;

@Log4j2
@Configuration
@ConditionalOnProperty(value = "qr-cache.enabled", havingValue = "true")
public class CacheStreamConfigurer implements BeanDefinitionRegistryPostProcessor,
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Set<String> cacheNames = new HashSet<>();

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        initCache();
        for (String cacheName : this.cacheNames) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CacheConsumer.class);
            registry.registerBeanDefinition(cacheName, builder.getBeanDefinition());
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistryPostProcessor.super.postProcessBeanFactory(beanFactory);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        initCache();
        try {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MutablePropertySources propertySources = environment.getPropertySources();
            Map<String, Object> newProperty = new HashMap<>();
            newProperty.put("spring.cloud.function.definition", String.join(";", this.cacheNames));
            for (String cacheName : this.cacheNames) {
                newProperty.put(this.propertyName(cacheName), this.destinationName(cacheName));
            }
            propertySources.addFirst(new MapPropertySource("Cache stream property", newProperty));
        } catch (Exception exception) {
            log.error("Cannot update environment " + exception.getMessage());
        }
    }

    private String propertyName(String cacheName) {
        return String.format("spring.cloud.stream.bindings.%s-out-0.destination", cacheName);
    }

    private String destinationName(String cacheName) {
        return String.format("%s-in-0", cacheName);
    }

    @SuppressWarnings("unchecked")
    private void initCache() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(CacheableMainRemoteServiceAbstract.class));
        scanner.addIncludeFilter(new AssignableTypeFilter(CacheableMainControllerAbstract.class));
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("com/quick/recording");
        for (BeanDefinition beanDefinition : candidateComponents) {
            try {
                Class<? extends MainController<?>> clazz =
                        (Class<? extends MainController<?>>)
                                Class.forName(beanDefinition.getBeanClassName());
                Constructor<?> constructor = Arrays.stream(clazz.getConstructors())
                        .findFirst()
                        .orElseThrow();
                Object[] param = new Object[constructor.getParameterCount()];
                Object object = constructor.newInstance(param);
                Collection<String> cacheNames = (Collection<String>) clazz.getMethod("listAllCacheName").invoke(object);
                this.cacheNames.addAll(cacheNames);
            } catch (Exception exception) {
                log.error("Cannot create consumer bean " + exception.getMessage());
            }
        }
    }

}
