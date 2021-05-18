package io.lw900925.ocean.support.mybatis.plugins;

import io.lw900925.ocean.support.spring.data.jpa.auditing.AnnotationAuditingMetadata;
import io.lw900925.ocean.support.spring.data.jpa.auditing.ReflectionAuditingBeanWrapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.auditing.AuditableBeanWrapper;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.domain.AuditorAware;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

/**
 * 提供类似Spring Data审计风格的功能。
 *
 * <p>
 *
 * 当发生数据库 {@code insert / update}行为时，自动填充{@code creator / creationDate / modifier / modifiedDate}等属性。
 *
 * @author lw900925
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })
})
public class AuditingInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyGenInterceptor.class);

    private AuditorAware<String> auditorProvider;
    private DateTimeProvider dateTimeProvider;

    private Properties properties;

    private final ConversionService conversionService;

    public AuditingInterceptor(AuditorAware<String> auditorProvider, DateTimeProvider dateTimeProvider) {
        // 初始化ConversionService
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        Jsr310Converters.getConvertersToRegister().forEach(conversionService::addConverter);
        this.conversionService = conversionService;
        this.auditorProvider = auditorProvider;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object object = invocation.getArgs()[1];

        // 获取AuditableBeanWrapper对象
        AuditableBeanWrapper<Object> auditableBeanWrapper = getBeanWrapperFor(object).orElse(null);
        if (auditableBeanWrapper == null) {
            LOGGER.warn("Cannot match AuditableBeanWrapper, please check annotation metadata config of '{}'", object.getClass());
            return invocation.proceed();
        }

        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            auditableBeanWrapper.setCreatedBy(auditorProvider.getCurrentAuditor().orElse(null));
            auditableBeanWrapper.setCreatedDate(dateTimeProvider.getNow().orElse(LocalDateTime.now()));
            auditableBeanWrapper.setLastModifiedBy(auditorProvider.getCurrentAuditor().orElse(null));
            auditableBeanWrapper.setLastModifiedDate(dateTimeProvider.getNow().orElse(LocalDateTime.now()));
        } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
            auditableBeanWrapper.setLastModifiedBy(auditorProvider.getCurrentAuditor().orElse(null));
            auditableBeanWrapper.setLastModifiedDate(dateTimeProvider.getNow().orElse(LocalDateTime.now()));
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Returns an {@link AuditableBeanWrapper} if the given object is capable of being equipped with auditing information.
     *
     * @param source the auditing candidate.
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<AuditableBeanWrapper<T>> getBeanWrapperFor(T source) {

        Assert.notNull(source, "Source must not be null!");

        return Optional.of(source).map(it -> {

            AnnotationAuditingMetadata metadata = AnnotationAuditingMetadata.getMetadata(it.getClass());

            if (metadata.isAuditable()) {
                return new ReflectionAuditingBeanWrapper<T>(conversionService, it);
            }

            return null;
        });
    }
}
