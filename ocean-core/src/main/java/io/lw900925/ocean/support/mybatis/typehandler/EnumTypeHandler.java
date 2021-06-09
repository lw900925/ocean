package io.lw900925.ocean.support.mybatis.typehandler;

import io.lw900925.ocean.support.enums.EnumAttribute;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@MappedJdbcTypes(value = JdbcType.TINYINT, includeNullJdbcType = true)
public class EnumTypeHandler extends BaseTypeHandler<EnumAttribute<Integer>> {

    private Class<EnumAttribute<Integer>> type;

    public EnumTypeHandler(Class<EnumAttribute<Integer>> type) {
        if (type == null) {
            throw new IllegalArgumentException("Argument 'type' cannot be null.");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EnumAttribute<Integer> parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public EnumAttribute<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return valueOf(rs.getInt(columnName));
    }

    @Override
    public EnumAttribute<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return valueOf(rs.getInt(columnIndex));
    }

    @Override
    public EnumAttribute<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return valueOf(cs.getInt(columnIndex));
    }

    private EnumAttribute<Integer> valueOf(Integer value) {
        EnumAttribute<Integer>[] types = type.getEnumConstants();
        return Arrays.stream(types).filter(type -> type.getValue().equals(value)).findFirst().orElse(null);
    }
}
