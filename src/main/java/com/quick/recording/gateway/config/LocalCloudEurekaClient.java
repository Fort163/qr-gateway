package com.quick.recording.gateway.config;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

@Component
@Primary
@Profile("local")
public class LocalCloudEurekaClient extends CloudEurekaClient {

    @Value("${spring.application.name}")
    private String appName;

    public LocalCloudEurekaClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, TransportClientFactories transportClientFactories, ApplicationEventPublisher publisher) {
        super(applicationInfoManager, config, transportClientFactories, publisher);
    }

    @Override
    public Applications getApplications() {
        super.getApplications().getRegisteredApplications().forEach(this::changeApplication);
        return super.getApplications();
    }

    private void changeApplication(Application application) {
        if(needChangeApplication(application)){
            try {
                List<InstanceInfo> instances = application.getInstances();
                for(InstanceInfo instanceInfo : instances){
                    String homePageUrlString = instanceInfo.getHomePageUrl()
                            .replace(instanceInfo.getHostName(),"localhost");
                    Field hostName = instanceInfo.getClass().getDeclaredField("hostName");
                    hostName.setAccessible(true);
                    Field homePageUrl = instanceInfo.getClass().getDeclaredField("homePageUrl");
                    homePageUrl.setAccessible(true);
                    hostName.set(instanceInfo,"localhost");
                    homePageUrl.set(instanceInfo,homePageUrlString);
                }
            }
            catch (Exception e){
            }
        }
    }

    private boolean needChangeApplication(Application application) {
        return !application.getName().toLowerCase(Locale.ROOT).equals(this.appName) &&
                application.getInstances().stream()
                    .anyMatch(instanceInfo -> !instanceInfo.getHostName().equals("localhost"));
    }
}
