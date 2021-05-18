package io.lw900925.ocean.support.spring.data.jpa.id;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import io.lw900925.ocean.core.keygen.SnowFlakeKeyGenerator;

import java.io.Serializable;

public class SnowFlakeIdentityGenerator implements IdentifierGenerator {

    private final SnowFlakeKeyGenerator keyGenerator = new SnowFlakeKeyGenerator();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return keyGenerator.nextId();
    }

    @Override
    public boolean supportsJdbcBatchInserts() {
        return true;
    }
}
