package com.mall.cloud.common.zk.registry.zookeeper;

import ch.qos.logback.core.util.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mall.cloud.common.config.properties.ZookeeperProperties;
import com.mall.cloud.common.zk.registry.base.CoordinatorRegistryCenter;
import com.mall.cloud.common.zk.registry.base.ReliableMessageRegisterDto;
import com.mall.cloud.common.zk.registry.exception.RegExceptionHandler;
import com.mall.cloud.provider.uac.common.base.constant.GlobalConstant;
import com.xiaoleilu.hutool.date.DateUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class ZookeeperRegistryCenter implements CoordinatorRegistryCenter {

    @Getter(AccessLevel.PROTECTED)
    private ZookeeperProperties zkConfig;

    private final Map<String, TreeCache> caches = new HashMap<>();

    @Getter
    private CuratorFramework client;

    @Getter
    private DistributedAtomicInteger distributedAtomicInteger;

    public ZookeeperRegistryCenter(final ZookeeperProperties zkConfig) {
        this.zkConfig = zkConfig;
    }

    @Override
    public void init() {
        log.debug("Elastic job: zookeeper registry center init, server lists is: {}.", zkConfig.getZkAddressList());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getZkAddressList())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds()));
        if (0 != zkConfig.getSessionTimeoutMilliseconds()) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeoutMilliseconds());
        }
        if (0 != zkConfig.getConnectionTimeoutMilliseconds()) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeoutMilliseconds());
        }
        if (Strings.isNullOrEmpty(zkConfig.getDigest())) {
            builder.authorization("digest", zkConfig.getDigest().getBytes(Charsets.UTF_8)).aclProvider(new ACLProvider() {
                @Override
                public List<ACL> getDefaultAcl() {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }

                @Override
                public List<ACL> getAclForPath(final String path) {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }
            });
        }
        client = builder.build();
        client.start();
        try {
            if (!client.blockUntilConnected(zkConfig.getMaxSleepTimeMilliseconds() * zkConfig.getMaxRetries(), TimeUnit.MILLISECONDS)) {
                client.close();
                throw new KeeperException.OperationTimeoutException();
            }
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(e);
        }
    }

    /**
     *  Gets directly.
     *
     * @param key 键
     *
     * @return
     */
    @Override
    public String getDirectly(final String key) {
        try {
            return new String(client.getData().forPath(key), Charsets.UTF_8);
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(e);
            return null;
        }
    }

    @Override
    public List<String> getChildrenKeys(final String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            result.sort(Comparator.reverseOrder());
            return result;
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            RegExceptionHandler.handleException(e);
            return Collections.emptyList();
        }
    }

    @Override
    public int getNumChildren(final String key) {
        Stat stat = null;
        try {
            stat = client.checkExists().forPath(key);
        } catch (Exception e) {
            RegExceptionHandler.handleException(e);
        }
        return stat == null ? 0 : stat.getNumChildren();
    }

    @Override
    public void persistEphemeral(String key, String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public String persistSequential(String key, String value) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
        return null;
    }

    @Override
    public void persistEphemeralSequential(String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
    }

    @Override
    public void addCacheData(String cachePath) {
        TreeCache cache = new TreeCache(client, cachePath);
        try {
            cache.start();
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(ex);
        }
        caches.put(cachePath + "/", cache);
    }

    @Override
    public void evictCacheData(String cachePath) {
        TreeCache cache = caches.remove(cachePath + "/");
        if (null != cache) {
            cache.close();
        }
    }

    @Override
    public Object getRawCache(String cachePath) {
        return caches.get(cachePath + "/");
    }

    @Override
    public void registerMq(String app, String host, String producerGroup, String consumerGroup, String namesrvAddr) {
        // 注册生产者
        final String producerRootPath = GlobalConstant.ZK_REGISTRY_PRODUCER_ROOT_PATH + GlobalConstant.Symbol.SLASH + app;
        final String consumerRootPath = GlobalConstant.ZK_REGISTRY_CONSUMER_ROOT_PATH + GlobalConstant.Symbol.SLASH + app;
        ReliableMessageRegisterDto dto;
        if (StringUtils.isNotEmpty(producerGroup)) {
            dto = new ReliableMessageRegisterDto().setProducerGroup(producerGroup).setNamesrvAddr(namesrvAddr);
            String producerJson = JSON.toJSONString(dto);
            this.persist(producerRootPath, producerJson);
            this.persistEphemeral(producerRootPath + GlobalConstant.Symbol.SLASH + host, DateUtil.now());
        }
        // 注册消费者
        if (StringUtils.isNotEmpty(consumerGroup)) {
            dto = new ReliableMessageRegisterDto().setConsumerGroup(consumerGroup).setNamesrvAddr(namesrvAddr);
            String producerJson = JSON.toJSONString(dto);
            this.persist(consumerRootPath, producerJson);
            this.persistEphemeral(consumerRootPath + GlobalConstant.Symbol.SLASH + host, DateUtil.now());
        }

    }

    @Override
    public void close() {
        for (Map.Entry<String, TreeCache> each : caches.entrySet()) {
            each.getValue().close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
    }

    /**
     * 等待500ms, cache先关闭再关闭client, 否则会抛异常
     * 因为异步处理, 可能会导致client先关闭而cache还未关闭结束.
     * 等待Curator新版本解决这个bug.
     * BUG地址：https://issues.apache.org/jira/browse/CURATOR-157
     */
    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     *
     * @param key 键
     *
     * @return
     */
    @Override
    public String get(final String key) {
        TreeCache cache = findTreeCache(key);
        if (null == cache) {
            return getDirectly(key);
        }
        ChildData resultInCache = cache.getCurrentData(key);
        if (null != resultInCache) {
            return null == resultInCache.getData() ? null : new String(resultInCache.getData(), Charsets.UTF_8);
        }
        return getDirectly(key);
    }

    private TreeCache findTreeCache(final  String key) {
        for (Map.Entry<String, TreeCache> entry : caches.entrySet()) {
            if (key.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean isExisted(final String key) {
        try {
            return null != client.checkExists().forPath(key);
        } catch (final Exception e) {
            RegExceptionHandler.handleException(e);
            return false;
        }
    }

    @Override
    public void persist(final String key, final String value) {
        try {
            if (!isExisted(key)) {
                if (null == value) {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key);
                } else {
                    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(Charsets.UTF_8));
                }
            } else {
                if (null != value) {
                    update(key, value);
                }
            }
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(e);
        }
    }

    @Override
    public void persist(String key) {
        this.persist(key, null);
    }

    @Override
    public void update(final String key, final String value) {
        try {
            client.inTransaction().check().forPath(key).and().setData().forPath(key, value.getBytes(Charsets.UTF_8)).and().commit();
            //CHECKSTYLE:OFF
        } catch (final Exception e) {
            //CHECKSTYLE:ON
            RegExceptionHandler.handleException(e);
        }
    }

    @Override
    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (final Exception e) {
            RegExceptionHandler.handleException(e);
        }
    }

    @Override
    public long getRegistryCenterTime(final String key) {
        long result = 0L;
        try {
            this.persist(key, "");
            result = client.checkExists().forPath(key).getMtime();
        } catch (final Exception e) {
            RegExceptionHandler.handleException(e);
        }
        Preconditions.checkArgument(0L != result, "Cannot get registry center time.");
        return result;
    }

    @Override
    public Object getRawClient() {
        return client;
    }

    @Override
    public void increment(String path, RetryNTimes retryNTimes) {
        try {
            distributedAtomicInteger = new DistributedAtomicInteger(client, path, retryNTimes);
            distributedAtomicInteger.increment();
        } catch (Exception e) {
            log.error("increment={}", e.getMessage(), e);
        }
    }

    @Override
    public AtomicValue<Integer> getAtomicValue(String path, RetryNTimes retryNTimes) {
        try {
            distributedAtomicInteger = new DistributedAtomicInteger(client, path, retryNTimes);
            return distributedAtomicInteger.get();
        } catch (Exception e) {
            log.error("getAtomicValue={}", e.getMessage(), e);
        }
        return null;
    }
}
