package org.matrixstudio.ocean.support.spring.data.jpa.usertype;

import java.util.Objects;

/**
 * <p>JPA Entity枚举类型属性接口。</p>
 *
 * @author liuwei
 */
public interface JpaEnumAttribute {

    /**
     * <p>获取枚举类的值</p>
     *
     * @return {@link Integer}
     */
    Integer getValue();

    /**
     * <p>将指定类型的枚举值转化成枚举类型。</p>
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <E>       指定的枚举类型，必须是{@link JpaEnumAttribute}的子类
     * @return {@link JpaEnumAttribute}的实体
     */
    default <E extends JpaEnumAttribute> E from(Class<E> enumClass, Integer value) {
        for (E enumObject : enumClass.getEnumConstants()) {
            if (Objects.equals(value, enumObject.getValue())) {
                return enumObject;
            }
        }
        throw new IllegalArgumentException("No enum value " + value + " found of " + enumClass.getCanonicalName());
    }
}