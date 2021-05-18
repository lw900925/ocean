package io.lw900925.ocean.support.spring.data.jpa.auditing;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.auditing.AuditableBeanWrapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * An {@link AuditableBeanWrapper} implementation that sets values on the target object using reflection.
 *
 * @author Oliver Gierke
 */
public class ReflectionAuditingBeanWrapper<T> implements AuditableBeanWrapper<T> {

    private final ConversionService conversionService;

    private final AnnotationAuditingMetadata metadata;
    private final T target;

    /**
     * Creates a new {@link ReflectionAuditingBeanWrapper} to set auditing data on the given target object.
     *
     * @param conversionService conversion service for date value type conversions
     * @param target must not be {@literal null}.
     */
    public ReflectionAuditingBeanWrapper(ConversionService conversionService, T target) {
        this.conversionService = conversionService;

        Assert.notNull(target, "Target object must not be null!");

        this.metadata = AnnotationAuditingMetadata.getMetadata(target.getClass());
        this.target = target;
    }

    @Override
    public Object setCreatedBy(Object value) {
        return setField(metadata.getCreatedByField(), value);
    }

    @Override
    public TemporalAccessor setCreatedDate(TemporalAccessor value) {
        return setDateField(metadata.getCreatedDateField(), value);
    }

    @Override
    public Object setLastModifiedBy(Object value) {
        return setField(metadata.getLastModifiedByField(), value);
    }

    @Override
    public Optional<TemporalAccessor> getLastModifiedDate() {
        return getAsTemporalAccessor(metadata.getLastModifiedDateField().map(field -> {

            Object value = org.springframework.util.ReflectionUtils.getField(field, target);
            return value instanceof Optional ? ((Optional<?>) value).orElse(null) : value;

        }), TemporalAccessor.class);
    }

    @Override
    public TemporalAccessor setLastModifiedDate(TemporalAccessor value) {
        return setDateField(metadata.getLastModifiedDateField(), value);
    }

    @Override
    public T getBean() {
        return target;
    }

    /**
     * Sets the given field to the given value if present.
     *
     * @param field
     * @param value
     */
    private <S> S setField(Optional<Field> field, S value) {

        field.ifPresent(it -> ReflectionUtils.setField(it, target, value));

        return value;
    }

    /**
     * Sets the given field to the given value if the field is not {@literal null}.
     *
     * @param field
     * @param value
     */
    private TemporalAccessor setDateField(Optional<Field> field, TemporalAccessor value) {

        field.ifPresent(it -> ReflectionUtils.setField(it, target, getDateValueToSet(value, it.getType(), it)));

        return value;
    }

    /**
     * Returns the {@link TemporalAccessor} in a type, compatible to the given field.
     *
     * @param value can be {@literal null}.
     * @param targetType must not be {@literal null}.
     * @param source must not be {@literal null}.
     * @return
     */
    @Nullable
    protected Object getDateValueToSet(TemporalAccessor value, Class<?> targetType, Object source) {

        if (TemporalAccessor.class.equals(targetType)) {
            return value;
        }

        if (conversionService.canConvert(value.getClass(), targetType)) {
            return conversionService.convert(value, targetType);
        }

        if (conversionService.canConvert(Date.class, targetType)) {

            if (!conversionService.canConvert(value.getClass(), Date.class)) {
                throw new IllegalArgumentException(
                        String.format("Cannot convert date type for member %s! From %s to java.util.Date to %s.", source,
                                value.getClass(), targetType));
            }

            Date date = conversionService.convert(value, Date.class);
            return conversionService.convert(date, targetType);
        }

        throw rejectUnsupportedType(source);
    }

    /**
     * Returns the given object as {@link TemporalAccessor}.
     *
     * @param source can be {@literal null}.
     * @param target must not be {@literal null}.
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <S extends TemporalAccessor> Optional<S> getAsTemporalAccessor(Optional<?> source,
                                                                             Class<? extends S> target) {

        return source.map(it -> {

            if (target.isInstance(it)) {
                return (S) it;
            }

            Class<?> typeToConvertTo = Stream.of(target, Instant.class)//
                    .filter(target::isAssignableFrom)//
                    .filter(type -> conversionService.canConvert(it.getClass(), type))//
                    .findFirst() //
                    .orElseThrow(() -> rejectUnsupportedType(source.map(Object.class::cast).orElseGet(() -> source)));

            return (S) conversionService.convert(it, typeToConvertTo);
        });
    }

    private static IllegalArgumentException rejectUnsupportedType(Object source) {
        return new IllegalArgumentException(String.format("Invalid date type %s for member %s! Supported types are %s.",
                source.getClass(), source, AnnotationAuditingMetadata.SUPPORTED_DATE_TYPES));
    }
}
