package com.quick.recording.gateway.test;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class TestCase<Type, Result> {

    private final Type object;
    private final TypeReference<Result> resultClass;
    private ResultRequest<Result> result;
    private final List<ApiCheckTestCase<Result>> tests = new ArrayList<>();

    public TestCase(Type object, TypeReference<Result> resultClass) {
        this.object = object;
        this.resultClass = resultClass;
    }

    public void setResult(ResultRequest<Result> result){
        this.result = result;
    }

    public void addTest(ApiCheckTestCase<Result> test){
        tests.add(test);
    }

    public void check(){
        if(Objects.isNull(result)){
            throw new RuntimeException("Result not set in test case");
        }
        tests.forEach(test -> test.check(result));
    }

}
