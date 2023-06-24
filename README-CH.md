<p align="center">
    <img width="400" src="https://gitee.com/d__isaster/cornucopia/raw/master/img/fast-mq.png">
</p>

## 🔥特性（Features）
- 🚀 开箱即用
- 🍄 延时队列
- 🔆 ACK机制
- 📦 异步通信
- 🎨 消息故障修复
- 🌕 死信队列处理
- 🌪️ 消息、消费组、消费者监控管理
- 💫 灵活接口幂等控制
- 🪐 支持redis单机、主从、集群
- ..........（待续）
## 🖥 环境要求 （Environment Required）
- redis v6.0.0+
- springboot v2.6.5
- jdk 1.8+
- ......

## 🌎 整体架构 （Architecture）

....（待续）

## ☀️ 快速开始（Quick Start）

### 依赖 (Dependency)

```java 
##此版本还未有监控页面
<dependency>
  <groupId>io.github.disaster1-tesk</groupId>
  <artifactId>fast-mq-core</artifactId>
  <version>1.3.0</version>
</dependency>
```
### 队列 (Queue)
#### 生产者 （Producer）
注入FastMQTemplate即可使用
```java 
public class FastMQTemplateTest extends BaseTest {
    @Autowired
    private FastMQTemplate fastMQTemplate;


    @Test
    public void sendMsgTest() {
        HashMap<String, Object> msg = Maps.newHashMap();
        msg.put("name", "disaster");
        msg.put("age", 20);
        fastMQTemplate.sendMsgAsync("disaster_topic", msg);
        fastMQTemplate.sendMsgAsync("disaster_topic", msg);
        fastMQTemplate.sendMsgAsync(FastMQConstant.DEFAULT_TOPIC, msg);
        while (true){

        }
    }
}

```
#### 消费者（Consumer）
```java 

/**
 * 不使用注解，则使用框架默认的topic和consumername
 * 
 */
@Service
@Slf4j
public class FastMQConsumerTest implements FastMQListener {
    @Override
    public void onMessage(Object o) {
        log.info("result = {}", o);
    }
}

/**
 * 使用注解可指定topic和consumername，同时还支持接口幂等处理
 * 
 */
@Service
@FastMQMessageListener(idempotent = true,groupName = "disaster",consumeName = "disaster1",topic = "disaster_topic", readSize = 0)
@Slf4j
public class FastMQConsumerAnnotationTest implements FastMQListener{
    @Override
    public void onMessage(Object t) {
        log.info("result = {}", t);
    }
}
```
### 延时队列 （Delay Queue）
#### 生产者 （Producer）
注入FastMQTemplate即可使用
```java 
public class FastMQDelayTemplateTest extends BaseTest {
    @Autowired
    private FastMQDelayTemplate fastMQDelayTemplate;

    @Test
    public void sendMsgTest() throws InterruptedException {
        Thread.sleep(2000l);
        fastMQDelayTemplate.msgEnQueue("hello", 20, null, TimeUnit.SECONDS);
        while (true) {
        }
    }
}

```
#### 消费者（Consumer）
```java 
/**
 * 不使用注解则使用框架默认队列名和线程池
 */
@Service
@Slf4j
public class FastMQDelayConsumerTest implements FastMQDelayListener {
    @Override
    public void onMessage(Object t) throws Throwable {
        log.info("result = {}", t);
    }
}

/**
 * 使用注解可自定义队列名称与线程池
 */
@FastMQDelayMessageListener(queueName = "test",executorName = "test_executor")
@Service
@Slf4j
public class FastMQDelayConsumerAnnotationTest implements FastMQDelayListener {
    @Override
    public void onMessage(Object t) throws Throwable {
        log.info("result = {}", t);
    }
}
```
##  💐 配置 （Configuration）
### 🦫Redission配置项
#### 1.fast-mq内置配置
fast-mq支持通过YAML配置Redission单机、主从、集群
```
## 单机版本
redisson:
  server:
    host: 127.0.0.1
    port: 6379
    database: 0
    deployment: stand_alone
## 主从版本
redisson:
  server:
    host: 127.0.0.1
    port: 6379
    database: 0
    nodes: 127.0.0.1:xxx,127.0.0.1:xxx,127.0.0.1:xxx
    master: mymaster
    deployment: master_slave
## 集群

  server:
    host: 127.0.0.1
    port: 6379
    database: 0
    nodes: 127.0.0.1:xxx,127.0.0.1:xxx,127.0.0.1:xxx
    deployment: cluster
```
#### 2.用户自定义
如果不想使用fast-mq提供的Redission-YAML配置，则只需要在springboot项目中实例化一个RedissonClient对象并被spring管理即可
```java
@Configuration
public class RedissionConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + "127.0.0.1:6379");
        singleServerConfig.setDatabase(1);
        singleServerConfig.setPassword("123456");
        return Redisson.create(config);
    }
}
```
### 🦦FastMQ配置项

```
fastmq:
  config:
    #是否开启fastmq
    enable: false
    # 每次拉取数据的量
    fetchMessageSize: 5
    #每次拉取PendingList的大小
    pullPendingListSize: 1000
    #死信门槛（秒）
    deadLetterThreshold: 32
    #是否从头开始订阅消息
    isStartFromHead: true
    #超过了该长度stream前面部分会被持久化（非严格模式——MAXLEN~）
    trimThreshold: 10000
    #是否是异步
    isAsync: false
    executor:
      #拉取默认主题信息的周期
      pullDefaultTopicMessagesPeriod: 10
      #检查PendingList周期
      pullTopicMessagesPeriod: 1
      time-unit: seconds
      #第一次延迟执行的时间
      initial-delay: 1
      #线程池的核心线程数，同步时调此参数能有效提高效率，如果采用的是异步消费的方式，使用默认配置即可
      executor-core-size: 20
    claim:
      #认领门槛(单位毫秒)
      claimThreshold: 20
      time-unit: milliseconds
    idle:
      #检查consumer不活跃的门槛（单位秒）
      pendingListIdleThreshold: 10
      time-unit: seconds
```

