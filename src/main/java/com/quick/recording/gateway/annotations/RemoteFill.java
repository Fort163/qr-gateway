package com.quick.recording.gateway.annotations;

import com.quick.recording.gateway.dto.SmartDto;

import java.lang.annotation.*;

/**
 * Support return type in method controller
 * <pre>List&lt;T&gt;</pre>
 * <pre>T</pre>
 * <pre>HttpEntity&lt;T&gt;</pre>
 * <pre>Page&lt;T&gt;</pre>
 * where T is the class containing the field with this annotation
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteFill {

    String fieldName();

    Class<? extends SmartDto> typeDto();


}
