package io.lw900925.ocean.core.keygen;

/**
 * 主键生成器
 *
 * @author lw900925
 */
public interface KeyGenerator {

    /**
     * 获取下一个序列
     * @return nextId
     */
    long nextId();
}
