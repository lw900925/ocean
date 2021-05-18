package io.lw900925.ocean.support.mybatis.plugins;

import io.lw900925.ocean.core.keygen.KeyGenerator;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * MyBatis主键生成逻辑，使用自定义生成器生成主键
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })
})
public class KeyGenInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyGenInterceptor.class);

    private Properties properties;

    private KeyGenerator keyGenerator;

    public KeyGenInterceptor(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object object = invocation.getArgs()[1];

        // 只处理insert方法
        if (ms.getSqlCommandType() != SqlCommandType.INSERT) {
            return invocation.proceed();
        }

        // 优先从insert配置中获取主键配置
        // 主键可能有多个（复合主键），这里默认单列主键
        String keyProperty = null;
        if (!Arrays.isNullOrContainsNull(ms.getKeyColumns()) && !Arrays.isNullOrContainsNull(ms.getKeyProperties())) {
            keyProperty = ms.getKeyProperties()[0];
        } else {
            // 从ResultMap中获取主键配置
            ResultMap resultMap = matchResultMap(ms, object);
            if (CollectionUtils.isEmpty(resultMap.getIdResultMappings())) {
                throw new NullPointerException(String.format("No id result mapping for '%s', please check your config.", object.getClass().getName()));
            }

            ResultMapping idResultMapping = resultMap.getIdResultMappings().get(0);
            keyProperty = idResultMapping.getProperty();
        }

        Assert.notNull(keyProperty, String.format("Key property cannot be null for '%s'", object.getClass().getName()));

        // 生成主键
        assignKey(object, keyProperty);

        return invocation.proceed();
    }

    /**
     * 匹配当前对象的ResultMap
     * @param ms {@link MappedStatement}
     * @param object insert object
     * @return {@link ResultMap}
     */
    private ResultMap matchResultMap(MappedStatement ms, Object object) {
        Configuration configuration = ms.getConfiguration();
        return configuration.getResultMaps().stream().filter(resultMap ->
            resultMap.getType() != null && resultMap.getType() == object.getClass()
        ).findFirst().orElseThrow(() -> new NullPointerException(String.format(
                "Cannot find any ResultMap for '%s', please check your config", object.getClass().getName())));
    }

    /**
     * 为对象生成ID
     * @param object insert object
     * @param keyProperties 主键属性名
     */
    private void assignKey(Object object, String keyProperties) {
        long id = keyGenerator.nextId();
        try {
            Field keyField = FieldUtils.getAllFieldsList(object.getClass()).stream()
                    .filter(field -> field.getName().equals(keyProperties))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchFieldException(String.format("No field '%s' declared in '%s'", keyProperties, object.getClass().getName())));
            FieldUtils.writeField(keyField, object, id, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Assign key error, no field '{}' in '{}'", keyProperties, object.getClass().getName());
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }
}
