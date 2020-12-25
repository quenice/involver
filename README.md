# Involver for Spring Boot

`Involver` 为基于Spring Boot框架的项目提供低侵入式的**执行流程管理**和**执行过程数据的收集**。

`Involver`目前提供以下功能的装配
- 日志记录
- Http请求执行
- 请求/响应加解密
- 时间获取

这些功能都是以 `Handler` 接口的形式，让使用者自行提供实现方式，而 `Involver` 根据配置，来判断是否装配这些功能。

而且`Involver`还是类型安全的

# 开始使用

## 安装
在`pom.xml`中加上以下依赖：

```
<dependency>
    <groupId>org.quenice</groupId>
    <artifactId>involver</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用

### 实现 `RequestHandler` 接口(required)

`RequestHandler` 的实现类提供`http`执行的逻辑。

你可以自行选择是使用 `Httpclient`, `HttpURLConnection` 或是其他任何组件

例子：

```java
@Service("rspHttpRequestHandler")
public class RspHttpRequestHandler implements RequestHandler {
    @Override
    public String handle(String url, String request, HttpMethod httpMethod, ExposedConfig exposedConfig) {
        String result = null;
        try {
            result = HttpUtils.doPost(url, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
```
 
### 实现 `CodecHandler` 接口(optional)

`CodecHandler` 的实现类提供对`请求参数`和`响应结果`进行编码解码(加解密)

注意：目前`involver`版本中，对`请求参数`和`响应结果`会自动使用`jackson`进行`json`转换。后续版本可能会提供Handler来让使用者自行决定如何处理

例子：

```java
@Service("rspHttpCodecHandler")
public class RspHttpCodecHandler implements CodecHandler {
    @Autowired
    private HttpApiCodec httpApiCodec;

    @Override
    public String encode(String value, ExposedConfig exposedConfig) throws CodecException {
        return httpApiCodec.encode(value);
    }

    @Override
    public String decode(String value, ExposedConfig exposedConfig) throws CodecException {
        return httpApiCodec.decode(value);
    }
}
```

### 实现 `LogHandler` 接口(optional)

`LogHandler` 的实现类提供整个请求过程的日志处理

例子：

```java
@Service("integrationHttpLogHandler")
public class IntegrationHttpLogHandler implements LogHandler {
    @Async
    @Override
    public void handle(RequestLog requestLog, ExposedConfig exposedConfig) {
        //把requestLog记录到DB
    }
}
```

### 实现 `TimeHandler` 接口(optional)

`TimeHandler` 的实现类用于获取当前时间。

由于在 `LogHandler` 中会记录下`请求时间`和`响应时间`，而且这两个时间可能会由用户最终保存到数据库，所以需要保证时间的准确性。

另外，`involver`提供了一个`DefaultSampleTimeHandler`默认实现

例子：

```java
@Service("integrationTimeHandler")
public class IntegrationTimeHandler implements TimeHandler {
    @Autowired
    private DateManager dateManager;

    @Override
    public long getTime() {
        return dateManager.getTime();
    }

    @Override
    public Date getDate() {
        return dateManager.getDate();
    }
}
```

### 编写业务接口
实现好以上 `Handler` 之后，接下来就要编写业务接口了

举例如下：

```java
@Http(url = "${custom.b2b.base-url}",
        method = HttpMethod.POST,
        codec = Flag.TRUE,
        logHandler = "integrationHttpLogHandler",
        codecHandler = "rspHttpCodecHandler",
        requestHandler = "rspHttpRequestHandler",
        timeHandler = "integrationTimeHandler",
        additional = "additional data")
public interface RspHttpService {
    @Http(url = "purchase", additional = "additional data")
    RspOutPurchaseResponse purchase(RspOutPurchaseRequest request);
    
    @Http
    String purchase1(String param, @Additional String additionalParam, @BaseUrl String baseUrl, @SubUrl String subUrl);
}
```

### 业务接口扫描配置

编写完业务接口之后，我们需要让业务接口被动态代理并生成代理对象，并交给spring容器管理，所以我们需要在`Spring Boot`的项目入口类做配置：

```java
@InvolverScan(basePackages = "site.neware.esim.service.integration", annotationClass = Http.class)
public class EsimApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsimApplication.class, args);
	}
}
```

### 调用
至此，`involver`的配置基本完成，接下来可以调用了

```java
@Service
public class TestService {
    @Autowired(required = false)
    private RspHttpService rspHttpService;
    
    public void test() {
        RspOutPurchaseRequest request = new RspOutPurchaseRequest();
        RspOutPurchaseResponse response = rspHttpService.purchase(request);
    }
}
```

## 详细配置
### @InvolverScan

`involver` 业务组件扫描配置，类似`mybatis`中的`MapperScan`

根据扫描配置，告诉`spring`如何扫描到业务接口，并把这些接口代理后装载到`spring`容器

以下三个参数可以配合使用，取交叉集

Properties|Required|Desc.
---|---|---
basePackages|否|扫描的包
annotationClass|否|扫描的接口上的注解
markerInterface|否|扫描的接口继承的接口

### @Http

`@Http` 可以配置在接口上，也能配置在方法上.

Properties|Required|Desc.
---|---|---
url|否|请求路径；支持字符串常量和`spring`配置文件表达式；如果配置在接口上，则代表基路径，如果配置在方法上，则代表子路径
method|否|http method
codec|否|http请求是否需要编码/解码
codecHandler|否|`spring bean`名字，继承自`CodecHandler`；用于处理请求参数/响应结果的加解密；如果`code=Flag.TRUE`，则必须配置该handler；方法上的配置会覆盖接口上的配置。
requestHandler|是|`spring bean`名字，继承自`RequestHandler`；用于处理HTTP请求的执行；方法上的配置会覆盖接口上的配置。
logHandler|否|`spring bean`名字，继承自`LogHandler`；用于处理整个请求/执行过程中的日志；方法上的配置会覆盖接口上的配置。
timeHandler|否|`spring bean`名字，继承自`TimeHandler`；用于获取当前时间；方法上的配置会覆盖接口上的配置。
additional|否|附加数据；用于在各`Handler`中做个性化判断

### @BaseUrl

`@BaseUrl` 注解的参数将覆盖接口上的`@Http`.`url`

### @SubUrl

`@SubUrl` 注解的参数将覆盖方法上的`@Http`.`url`

### @Additional

`@Additional` 注解的参数会作为`ExposedConfig`的属性，暴露于各个`Handler`的方法中

# 依赖兼容性
## JDK 版本

`>= 1.7`

## Spring Boot 版本

`>=1.5.3.RELEASE`

## Spring 版本

`>=4.3.8.RELEASE`

## Jackson版本

`>=2.8.8`
