package io.lw900925.ocean.support.enums;

import java.util.Objects;

/**
 * <p>Entity枚举类型属性接口。</p>
 *
 * @author liuwei
 */
public interface EnumAttribute<T> {

    /**
     * <p>获取枚举类的值</p>
     *
     * @return {@link Integer}
     */
    T getValue();

    /**
     * <p>将指定类型的枚举值转化成枚举类型。</p>
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <E>       指定的枚举类型，必须是{@link EnumAttribute}的子类
     * @return {@link EnumAttribute}的实体
     */
    default <E extends EnumAttribute<T>> E from(Class<E> enumClass, T value) {
        for (E enumObject : enumClass.getEnumConstants()) {
            if (Objects.equals(value, enumObject.getValue())) {
                return enumObject;
            }
        }
        throw new IllegalArgumentException("No enum value " + value + " found of " + enumClass.getCanonicalName());
    }
}