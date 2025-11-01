package com.quick.recording.gateway.config.validation.remote;

import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.main.service.remote.CacheableMainRemoteServiceAbstract;
import com.quick.recording.gateway.main.service.remote.MainRemoteService;
import com.quick.recording.gateway.main.service.remote.MainRemoteServiceAbstract;
import com.quick.recording.gateway.util.ContextUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class CheckRemoteValidator implements ConstraintValidator<CheckRemote, UUID> {

    private final ApplicationContext context;

    public CheckRemoteValidator(ApplicationContext context) {
        this.context = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(uuid)){
            return true;
        }
        Map<String, Object> attributes = ((ConstraintValidatorContextImpl) constraintValidatorContext)
                .getConstraintDescriptor().getAttributes();
        Object object = attributes.get("typeDto");
        if(Objects.nonNull(object) && object instanceof Class){
            try {
                Class<? extends SmartDto> dtoClass = (Class<? extends SmartDto>)object;
                MainRemoteService<? extends SmartDto> service = ContextUtil.findService(getContext(), dtoClass);
                if(Objects.isNull(service)){
                    return false;
                }
                return service.byUuid(uuid).getStatusCode().is2xxSuccessful();
            }
            catch (Exception exception){
                return false;
            }
        }
        return false;
    }

    public ApplicationContext getContext() {
        return context;
    }

}
