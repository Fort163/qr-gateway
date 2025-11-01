package com.quick.recording.gateway.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.util.ReflectUtil;
import com.quick.recording.resource.service.security.SSOService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestContextHolder.class)
public abstract class MainTestController<Type extends BaseDto> {

    /*
        Start form 1
        @Order(+5).
        Шаг между тестами использовать для собственных тестов
     */

    protected TypeReference<ApiError> typeError = new TypeReference<ApiError>() {};
    private TestAuthHelper authHelper;
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private TestContextHolder<Type> testContextHolder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SSOService ssoService;

    @BeforeEach
    public void beforeTest() {
        this.authHelper = new TestAuthHelper(ssoService);
        restTemplate = new TestRestTemplate(new RestTemplateBuilder().rootUri("http://localhost:" + port));
    }

    @Test
    @Order(0)
    public void init() {
        assertThat(port).isNotNull();
        assertThat(restTemplate).isNotNull();
        assertThat(authHelper).isNotNull();
        assertThat(testContextHolder).isNotNull();
        assertThat(objectMapper).isNotNull();
    }

    @Test
    @Order(10)
    void authServerForTest() {
        assertThat(authHelper.token()).isNotNull();
    }

    @Test
    @Order(15)
    @SuppressWarnings("unchecked")
    public void testPost() {
        List<TestCase<Type, ?>> testCases = postGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        uri(),
                        HttpMethod.POST,
                        createData(testCase.getObject()),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());

                if(Objects.nonNull(resultObject) && BaseDto.class.isAssignableFrom(resultObject.getClass())){
                    testContextHolder.setLastCreateObject((Type) resultObject);
                }
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(20)
    public void testByUUIDBeforeDelete() {
        List<TestCase<Type, ?>> testCases = byUUIDBeforeDeleteGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        createUriWithUUID(uri(),testCase.getObject().getUuid()),
                        HttpMethod.GET,
                        createData(null),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(25)
    @SuppressWarnings("unchecked")
    public void testHardDelete() {
        List<TestCase<Type, ?>> testCases = deleteHardGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        createUriWithUUID(uri(),testCase.getObject().getUuid()) + "?delete=HARD",
                        HttpMethod.DELETE,
                        createData(null),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                if(Objects.nonNull(resultObject) && BaseDto.class.isAssignableFrom(resultObject.getClass())){
                    testContextHolder.setLastDeletedObject((Type) resultObject);
                }
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(30)
    public void testByUUIDAfterDelete() {
        List<TestCase<Type, ?>> testCases = byUUIDAfterDeleteGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        createUriWithUUID(uri(),testCase.getObject().getUuid()),
                        HttpMethod.GET,
                        createData(null),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(35)
    @SuppressWarnings("unchecked")
    public void testNewPost() {
        List<TestCase<Type, ?>> testCases = postGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        uri(),
                        HttpMethod.POST,
                        createData(testCase.getObject()),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());

                if(Objects.nonNull(resultObject) &&
                        BaseDto.class.isAssignableFrom(resultObject.getClass())){
                    testContextHolder.setLastCreateObject((Type) resultObject);
                }
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(40)
    public void testList() {
        List<TestCase<Type, ?>> testCases = listGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        createUri(uri(),testCase.getObject()),
                        HttpMethod.GET,
                        createData(null),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(45)
    public void testByUUIDAfterNewPostDelete() {
        List<TestCase<Type, ?>> testCases = byUUIDBeforeDeleteGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        createUriWithUUID(uri(),testCase.getObject().getUuid()),
                        HttpMethod.GET,
                        createData(null),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(50)
    @SuppressWarnings("unchecked")
    public void testPut() {
        List<TestCase<Type, ?>> testCases = putGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        uri(),
                        HttpMethod.PUT,
                        createData(testCase.getObject()),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                if(Objects.nonNull(resultObject) &&
                        BaseDto.class.isAssignableFrom(resultObject.getClass())){
                    testContextHolder.setLastCreateObject((Type) resultObject);
                }
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(55)
    @SuppressWarnings("unchecked")
    public void testPatch() {
        List<TestCase<Type, ?>> testCases = patchGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        uri() + "/patch",
                        HttpMethod.PUT,
                        createData(testCase.getObject()),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                if(Objects.nonNull(resultObject) &&
                        BaseDto.class.isAssignableFrom(resultObject.getClass())){
                    testContextHolder.setLastCreateObject((Type) resultObject);
                }
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(60)
    public void testSoftDelete() {
        List<TestCase<Type, ?>> testCases = deleteSoftGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        createUriWithUUID(uri(),testCase.getObject().getUuid()) + "?delete=SOFT",
                        HttpMethod.DELETE,
                        createData(null),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(65)
    public void testRestore() {
        List<TestCase<Type, ?>> testCases = restoreGetTestCases();
        if (Objects.nonNull(testCases)) {
            for (TestCase<Type, ?> testCase : testCases) {
                ResponseEntity<String> response = restTemplate.exchange(
                        createUriWithUUID(uri(),testCase.getObject().getUuid()),
                        HttpMethod.PUT,
                        createData(null),
                        String.class
                );
                Object resultObject = convertResponse(response.getBody(), testCase.getResultClass());
                testCase.setResult(new ResultRequest(resultObject, response.getStatusCode(), response));
                testCase.check();
            }
        }
    }

    @Test
    @Order(Integer.MAX_VALUE)
    public void clear(){
        getTestContextHolder().setLastCreateObject(null);
        getTestContextHolder().setLastDeletedObject(null);
        if(!getTestContextHolder().isSuite()) {
            for (MainService<? extends SmartEntity, ? extends SmartDto> service : getServicesForClear()){
                clearService(service);
            }
        }
    }

    @NonNull
    public abstract List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear();

    @NonNull
    public abstract String uri();

    @NonNull
    public abstract List<TestCase<Type, ?>> postGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> byUUIDBeforeDeleteGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> deleteHardGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> byUUIDAfterDeleteGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> listGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> putGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> patchGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> deleteSoftGetTestCases();

    @NonNull
    public abstract List<TestCase<Type, ?>> restoreGetTestCases();

    @Nullable
    private <T> T convertResponse(Object object, TypeReference<T> typeReference) {
        try {
            String string = null;
            if(object instanceof String){
                string = object.toString();
            }
            else {
                string = objectMapper.writeValueAsString(object);
            }
            return objectMapper.readValue(string, typeReference);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private String createUri(String uri, Type type) {
        String uriNew = uri + "?";
        try {
            List<Field> fields = ReflectUtil.classTypeFieldFromClass(type.getClass(), String.class);
            fields.addAll(ReflectUtil.classTypeFieldFromClass(type.getClass(), Number.class));
            fields.addAll(ReflectUtil.classTypeFieldFromClass(type.getClass(), Boolean.class));
            fields.addAll(ReflectUtil.classTypeFieldFromClass(type.getClass(), Enum.class));
            fields.addAll(ReflectUtil.classTypeFieldFromClass(type.getClass(), UUID.class));
            ReflectUtil.DataWorkerList dataWorker = ReflectUtil.getDataWorker(fields, type.getClass());
            for (Field field : fields) {
                Object invoke = dataWorker.getDataWorker(field.getName()).getGetter().invoke(type);
                if(Objects.nonNull(invoke)){
                    uriNew += field.getName() + "=" + invoke.toString() + "&";
                }
            }
        } catch (Exception ignore) {
        }
        return uriNew.substring(0, uriNew.length() - 1);
    }

    private String createUriWithUUID(String uri, UUID uuid) {
        if(Objects.nonNull(uuid)) {
            return uri + "/" + uuid.toString();
        }
        else {
            return uri + "/null";
        }
    }

    private HttpEntity<String> createData(@Nullable Type dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authHelper.token());
        if(Objects.isNull(dto)){
            return new HttpEntity<>(headers);
        }
        try {
            return new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        }
        catch (JsonProcessingException exception){
            return new HttpEntity<>(headers);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public Type getLastDeletedObjectClone(){
        return createClone(this.getTestContextHolder().getLastDeletedObject());
    }

    @Nullable
    public Type getLastCreateObjectClone(){
        return createClone(this.getTestContextHolder().getLastCreateObject());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private Type createClone(Type type) {
        if(Objects.isNull(type)){
            return null;
        }
        try {
            Class<? extends BaseDto> aClass = type.getClass();
            String jsonString = objectMapper.writeValueAsString(type);
            return (Type) objectMapper.readValue(jsonString, aClass);
        }
        catch (JsonProcessingException exception){
            return null;
        }
    }

    public TestContextHolder<Type> getTestContextHolder() {
        return testContextHolder;
    }

    @Nullable
    public <T extends SmartDto> T getDtoFromService(MainService<? extends SmartEntity, T> service){
        T result = null;
        List<? extends SmartEntity> all = service.findAll();
        if(Objects.nonNull(all) && !all.isEmpty()){
            result = service.byUuid(all.get(0).getUuid());
        }
        return result;
    }

    public <T extends SmartDto> List<T> getDtoListFromService(MainService<? extends SmartEntity, T> service){
        List<T> result = new ArrayList<>();
        List<? extends SmartEntity> all = service.findAll();
        if(Objects.nonNull(all)) {
            for (SmartEntity entity : all) {
                result.add(service.byUuid(entity.getUuid()));
            }
        }
        return result;
    }

    private void clearService(MainService<? extends SmartEntity, ? extends SmartDto> service){
        List<? extends SmartEntity> all = service.findAll();
        if (Objects.nonNull(all) && !all.isEmpty()) {
            service.deleteAll(all.stream().map(SmartEntity::getUuid).collect(Collectors.toList()));
        }
    }

}
