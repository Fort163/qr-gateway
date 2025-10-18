package com.quick.recording.gateway.config.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.recording.gateway.config.MessageUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.authorization.ExpressionAuthorizationDecision;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.*;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class QRExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;

    private final ApplicationContext context;
    private final ObjectMapper jacksonObjectMapper;
    private final MessageUtil messageUtil;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(messageUtil.create("error.validation.field", fieldName, errorMessage));
        });
        String messageError = messageUtil.create("error.validation.class",
                ex.getParameter().getExecutable().getDeclaringClass().getSimpleName(),
                ex.getParameter().toString(),
                ex.getParameter().getParameter().toString()
        );
        ApiError build = ApiError.builder().message(messageError).errors(errors).service(serviceName).build();
        return new ResponseEntity(build, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.getMessage())
                .message(
                        messageUtil.create("exception.missing.parameter.request",
                                ex.getMethodParameter().getParameter().getName(),
                                ex.getMethodParameter().getParameter().getType().getName(),
                                ex.getMethodParameter().getExecutable().toString()
                        )
                )
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.BAD_REQUEST);
    }

    @Nullable
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatusCode status,
                                                        WebRequest request) {
        String methodName = "";
        if(ex instanceof MethodArgumentTypeMismatchException){
            methodName = ((MethodArgumentTypeMismatchException) ex).getParameter().getMethod().getName();
        }
        else {
            if(request instanceof ServletWebRequest) {
                methodName = ((ServletWebRequest) request).getRequest().getServletPath();
            }
        }
        ApiError build = ApiError.builder()
                .debugMessage(ex.getMessage())
                .message(messageUtil.create("exception.mismatch.parameter.request",
                                ex.getPropertyName(),
                                ex.getRequiredType().toString(),
                                ex.getValue().toString(),
                                methodName
                        )
                )
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.getMessage())
                .message(
                        messageUtil.create("exception.missing.parameter.path",
                                ex.getParameter().getParameter().getName(),
                                ex.getParameter().getParameter().getType().getName(),
                                ex.getParameter().getExecutable().toString()
                        )
                )
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                    HttpHeaders headers,
                                                                    HttpStatusCode status,
                                                                    WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.getMessage())
                .message(
                        messageUtil.create("exception.not.found.resource",
                                ex.getResourcePath(),
                                ex.getHttpMethod().name()
                        )
                )
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex,
                                                       HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        Optional<ByteBuffer> byteBuffer = ex.responseBody();
        if (byteBuffer.isPresent() && byteBuffer.get().hasArray()) {
            result = new String(byteBuffer.get().array());
        }
        ApiError apiError = null;
        if (!result.isEmpty()) {
            try {
                apiError = jacksonObjectMapper.readValue(result, ApiError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        String messageError = messageUtil.create("error.in.endpoint", request.getServletPath());
        ApiError build = ApiError.builder().message(messageError).service(serviceName).parentError(apiError).build();
        if (Objects.isNull(apiError)) {
            build.setDebugMessage(ex.getMessage());
        }
        return new ResponseEntity<Object>(build, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.toString())
                .message(ex.getMessage())
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        try {
            String methodAuthority = null;
            String authority = null;
            String spellThis = "#root.this.";
            String userName = request.getUserPrincipal().getName();
            String path = ((ServletWebRequest) request).getRequest().getServletPath();
            if (ex instanceof AuthorizationDeniedException) {
                AuthorizationResult result = ((AuthorizationDeniedException) ex).getAuthorizationResult();
                if (result instanceof ExpressionAuthorizationDecision) {
                    String expression = ((ExpressionAuthorizationDecision) result).getExpression().getExpressionString();
                    if (expression.contains(spellThis)) {
                        methodAuthority = expression.substring(expression.lastIndexOf(spellThis) + spellThis.length(),
                                expression.lastIndexOf("("));
                    } else {
                        if (expression.contains("('")) {
                            authority = expression.substring(expression.indexOf("'"), expression.lastIndexOf("'"));
                        }
                    }
                }
            }
            if (Objects.isNull(authority) && Objects.nonNull(methodAuthority)) {
                HandlerMethod method = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE,
                        WebRequest.SCOPE_REQUEST);
                Method searchAuthority = method.getBeanType().getMethod(methodAuthority);
                if (searchAuthority.getReturnType().isArray()) {
                    Object bean = context.getBean(method.getBean().toString());
                    authority = Strings.join(Arrays.asList((Object[]) searchAuthority.invoke(bean)), ',');
                }
            }
            ApiError build;
            if (Objects.nonNull(authority) && Objects.nonNull(userName) && Objects.nonNull(path)) {
                build = ApiError.builder()
                        .debugMessage(ex.toString())
                        .message(messageUtil.create("error.access.denied.with.info", userName, authority, path))
                        .service(serviceName)
                        .build();
            } else {
                build = ApiError.builder()
                        .debugMessage(ex.toString())
                        .message(messageUtil.create("error.access.denied"))
                        .service(serviceName)
                        .build();
            }
            return new ResponseEntity(build, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            ApiError build = ApiError.builder()
                    .debugMessage(ex.toString())
                    .message(messageUtil.create("error.access.denied"))
                    .service(serviceName)
                    .build();
            return new ResponseEntity(build, HttpStatus.FORBIDDEN);
        }
    }

}
