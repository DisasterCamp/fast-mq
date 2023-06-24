<p align="center">
    <img width="400" src="https://gitee.com/d__isaster/cornucopia/raw/master/img/fast-mq.png">
</p>

[Chinese](https://github.com/DisasterCamp/lock-layer/blob/master/README-CH.md)

## 🔥Features

- 🚀 Out of the box
- 🍄 Delay queue
- 🔆 ACK mechanism
- 📦 Asynchronous communication
- 🎨 Message fault repair
- 🌕 Dead letter queue processing
- 🌪️ Messages, consumer groups, consumer monitoring management
- 💫 Flexible interface idempotent control
- 🪐 Supports redis single-node, master-slave, and cluster
- ..........（To be continued）
## 🖥 Environment Required
- redis v6.0.0+
- springboot v2.6.5
- jdk 1.8+
- ......

## 🌎 Architecture

....（To be continued）

## ☀️ Quick Start

### Dependency

```java 
# This version does not yet have a monitoring page
<dependency>
  <groupId>io.github.disaster1-tesk</groupId>
  <artifactId>fast-mq-core</artifactId>
  <version>1.3.0</version>
</dependency>
```
### Queue
#### Producer
Inject FastMQTemplate to use it
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
#### Consumer
```java 

/**
 * Without annotations, the framework's default topic and consumername are used
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
 * Use annotations to specify topic and consumername, and also support interface idempotent processing
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
### Delay Queue
#### Producer
Inject FastMQTemplate to use it
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
#### Consumer
```java 
/**
 * Use the framework default queue name and thread pool without annotations
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
 * Use annotations to customize the queue name and thread pool
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
##  💐 Configuration
### 🦫Redission Configuration
#### 1.fast-mq Built-in configuration
fast-mq supports the Redission single-node, master-slave, and cluster configuration using YAML
```
## Stand-alone version
redisson:
  server:
    host: 127.0.0.1
    port: 6379
    database: 0
    deployment: stand_alone
## Master-slave version
redisson:
  server:
    host: 127.0.0.1
    port: 6379
    database: 0
    nodes: 127.0.0.1:xxx,127.0.0.1:xxx,127.0.0.1:xxx
    master: mymaster
    deployment: master_slave
## cluster
  server:
    host: 127.0.0.1
    port: 6379
    database: 0
    nodes: 127.0.0.1:xxx,127.0.0.1:xxx,127.0.0.1:xxx
    deployment: cluster
```
#### 2.Customize
If you don't want to use the Redission-YAML configuration provided by fast-mq, you just need to instantiate a RedissonClient object in your springboot project and have it managed by spring
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
### 🦦FastMQ Configuration

```
fastmq:
  config:
    # Whether to enable fastmq
    enable: false
    # The amount of data pulled each time
    fetchMessageSize: 5
    # Pull the size of the PendingList each time
    pullPendingListSize: 1000
    # Dead letter threshold (seconds)
    deadLetterThreshold: 32
    # Whether to subscribe to messages from the beginning
    isStartFromHead: true
    # The stream is persisted beyond this length (non-strict mode -- MAXLEN~).
    trimThreshold: 10000
    # Asynchronous or not
    isAsync: false
    executor:
      # Pull the period of the default topic information
      pullDefaultTopicMessagesPeriod: 10
      # Check the PendingList period
      pullTopicMessagesPeriod: 1
      time-unit: seconds
      # Delay time for the first execution
      initial-delay: 1
      # Number of core threads in the thread pool. Set this parameter during synchronization to improve efficiency. If the asynchronous consumption mode is used, use the default value
      executor-core-size: 20
    claim:
      # Claim threshold (milliseconds)
      claimThreshold: 20
      time-unit: milliseconds
    idle:
      # Check the threshold for consumer inactivity (seconds)
      pendingListIdleThreshold: 10
      time-unit: seconds
```

