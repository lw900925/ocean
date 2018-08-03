package org.matrixstudio.ocean.support.spring.data.jpa.auditing;

import org.springframework.data.auditing.DateTimeProvider;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class LocalDateTimeProvider implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(LocalDateTime.now());
    }
}
