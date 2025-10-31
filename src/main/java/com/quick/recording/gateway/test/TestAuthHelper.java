package com.quick.recording.gateway.test;

import com.quick.recording.resource.service.security.SSOService;
import org.springframework.security.access.AuthorizationServiceException;

import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Objects;

public class TestAuthHelper {

    private String token = null;

    public TestAuthHelper(SSOService ssoService) {
        if (Objects.nonNull(ssoService)) {
            try {
                token = ssoService.getSSOResult().token();
            } catch (NoSuchElementException exception) {
            } catch (URISyntaxException | AuthorizationServiceException exception) {
                exception.printStackTrace();
            }
        }
    }

    public String token() {
        return token;
    }
}
