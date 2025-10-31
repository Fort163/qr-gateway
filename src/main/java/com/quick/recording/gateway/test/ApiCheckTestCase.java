package com.quick.recording.gateway.test;

@FunctionalInterface
public interface ApiCheckTestCase<T> {

    void check(ResultRequest<T> result);

}
