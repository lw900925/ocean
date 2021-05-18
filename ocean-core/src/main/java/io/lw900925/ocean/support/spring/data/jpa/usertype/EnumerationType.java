package io.lw900925.ocean.support.spring.data.jpa.usertype;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import io.lw900925.ocean.support.enums.EnumAttribute;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

/**
 * <p>Hibernate JPA自定义类型，持久化枚举类。</p>
 *
 * @author liuwei
 */
@SuppressWarnings("unchecked")
public class EnumerationType implements UserType, DynamicParameterizedType {

    private Class<?> enumClass;

    /**
     * <p>枚举类映射的类型</p>
     */
    private static final int[] SQL_TYPES = new int[]{Types.INTEGER};

    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);
        if (reader != null) {
            enumClass = reader.getReturnedClass().asSubclass(Enum.class);
        }
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        // 不相等的条件才会出发JPA Update操作
        if (x == null && y == null) {
            return true;
        }

        if ((x == null && y != null) || (x != null && y == null)) {
            return false;
        }

        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        // 返回枚举类
        String value = rs.getString(names[0]);
        if (value == null) {
            return null;
        }

        for (Object object : enumClass.getEnumConstants()) {
            if (Objects.equals(Integer.parseInt(value), ((EnumAttribute<Integer>) object).getValue())) {
                return object;
            }
        }

        throw new RuntimeException(String.format("Unknown name value [%s] for enum class [%s]", value, enumClass.getName()));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, SQL_TYPES[0]);
        } else if (value instanceof Integer) {
            st.setInt(index, (Integer) value);
        } else {
            st.setInt(index, ((EnumAttribute<Integer>) value).getValue());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
