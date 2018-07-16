package com.mall.cloud.common.zk.registry;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.mall.cloud.common.config.properties.AliyunProperties;
import com.mall.cloud.common.config.properties.MallCloudProperties;
import com.mall.cloud.common.config.properties.ZookeeperProperties;
import com.mall.cloud.common.zk.generator.IncrementIdGenerator;
import com.mall.cloud.common.zk.registry.base.CoordinatorRegistryCenter;
import com.mall.cloud.common.zk.registry.base.RegisterDto;
import com.mall.cloud.common.zk.registry.zookeeper.ZookeeperRegistryCenter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心工厂
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistryCenterFactory {

    private static final ConcurrentHashMap<HashCode, CoordinatorRegistryCenter> REG_CENTER_REGISTRY = new ConcurrentHashMap<>();

    /**
     * 创建注册中心.
     * @param zookeeperProperties
     * @return 注册中心对象
     */
    public static CoordinatorRegistryCenter createCoordinatorRegistryCenter(ZookeeperProperties zookeeperProperties) {
        Hasher hasher = Hashing.md5().newHasher().putString(zookeeperProperties.getZkAddressList(), Charsets.UTF_8);
        HashCode hashCode = hasher.hash();
        CoordinatorRegistryCenter result =  REG_CENTER_REGISTRY.get(hashCode);
        if (null != result) {
            return result;
        }
        result = new ZookeeperRegistryCenter(zookeeperProperties);
        result.init();
        REG_CENTER_REGISTRY.put(hashCode, result);
        return result;
    }

    /**
     * 启动注册中心
     * @param mallCloudProperties
     * @param host
     * @param app
     */
    public static void startup(MallCloudProperties mallCloudProperties, String host, String app) {
        CoordinatorRegistryCenter coordinatorRegistryCenter = createCoordinatorRegistryCenter(mallCloudProperties.getZk());
        RegisterDto registerDto = new RegisterDto(app, host, coordinatorRegistryCenter);
        Long serviceId = new IncrementIdGenerator(registerDto).nextId();
        IncrementIdGenerator.setServiceId(serviceId);
        registerMq(mallCloudProperties, host, app);
    }

    /**
     * 注册 aliyun RocketMq.
     * @param mallCloudProperties
     * @param host
     * @param app
     */
    private static void registerMq(MallCloudProperties mallCloudProperties, String host, String app) {
        CoordinatorRegistryCenter coordinatorRegistryCenter = createCoordinatorRegistryCenter(mallCloudProperties.getZk());
        AliyunProperties.RocketMqProperties rocketMqProperties = mallCloudProperties.getAliyun().getRocketMq();
        String consumerGroup = rocketMqProperties.isReliableMessageConsumer() ? rocketMqProperties.getConsumerGroup() : null;
        String namesrvAddr = rocketMqProperties.getNamesrvAddr();
        String producerGroup = rocketMqProperties.isReliableMessageProducer() ? rocketMqProperties.getProducerGroup() : null;
        coordinatorRegistryCenter.registerMq(app, host, producerGroup, consumerGroup, namesrvAddr);
    }
}
