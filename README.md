# Involver for Spring Boot

`Involver` is http component for Spring Boot

它能够为基于Spring Boot框架的项目提供低侵入式的Http访问服务。

`Involver`提供以下功能
- 日志记录
- Http访问执行
- 访问加解密
- 时间获取

但是注意，这些功能都是以 Handler 接口的形式，让使用者自行提供实现方式，而`Involver`根据配置，来判断是否装配这些功能

而且`Involver`还是类型安全的

# Getting started

## installation
在`pom.xml`中加上以下依赖：

```
<dependency>
    <groupId>org.quenice</groupId>
    <artifactId>involver</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### Implement `RequestHandler`(required)

`RequestHandler` 的实现类提供`http`执行的逻辑。

你可以自行选择是使用 `Httpclient`, `HttpURLConnection` 或是其他任何组件
 
### Implement `CodecHandler` (optional)

`CodecHandler` 的实现类提供对`请求参数`和`响应结果`进行编码解码(加解密)

注意：目前`involver`版本中，对`请求参数`和`响应结果`会自动使用`jackson`进行`json`转换。后续版本可能会提供Handler来让使用者自行决定如何处理

### Implement `LogHandler` (optional)

`LogHandler` 的实现类提供整个请求过程的日志处理


### Implement `TimeHandler` (optional)

`TimeHandler` 的实现类用于获取当前时间。

由于在 `LogHandler` 中会记录下`请求时间`和`响应时间`，而且这两个时间可能会由用户最终保存到数据库，所以需要保证时间的准确性。

另外，`involver`提供了一个`DefaultSampleTimeHandler`默认实现

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
        additional = "additional data")
public interface RspHttpService {
    @Http(url = "purchase", additional = "additional data")
    RspOutPurchaseResponse purchase(RspOutPurchaseRequest request);
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

