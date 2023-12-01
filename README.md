# edelweiss-logging
## 1. 特性
### 1.1 基于AOP注解
在方法上或类上加日志注解使用
```java
@ELog(successTemplate = "新建用户")
public void addUser(){}
```
#### 1.1.1 失败日志
如果方法正常执行，则使用successTemplate模板，同时可以指定failTemplate，默认当方法抛出异常，且failTemplate不为空，会使用失败日志
```java
@ELog(successTemplate = "新建用户", failTemplate = "新建用户失败")
public void addUser(){}
```
#### 1.1.2 业务类型和标签
可以对日志标记业务类型（单个）和标签（多个）

标签使用=分隔key和value

> [!NOTE]
> 如何处理业务类型和标签由用户在日志执行器中自行决定
> 
> 标签只允许有一个=号，多个=号的标签会被忽略
```java
@ELog(bizType = "user-service", tags = {"opt=create", "target=user"}, successTemplate = "新建用户")
public void addUser(){}
```
> [!NOTE]
> 全局配置，类注解，方法注解的tag最终会取并集
> 
> 业务类型方法注解会覆盖类注解，覆盖全局配置
#### 1.1.3 日志主体和租户
通常是登录用户的用户名等信息，可以手动在注解中指定，也可以配合内置的spring拦截器，提供对应接口实现类来全局获取
```java
@ELog(group = "default-group", subject = "default-user", successTemplate = "新建用户", failTemplate = "新建用户失败")
public void addUser(){}
```
用户信息获取接口，实现并注册为spring bean，会被内置的拦截器自动调用
```java
public interface UserAuthService {
    //是否登录
    boolean isLogin();
    //租户信息，默认作为group
    String getTenant();
    //用户信息，默认作为subject
    String getUser();
}
```
### 1.2 模板解析
日志模板支持字面量，spel表达式，自定义方法（需要实现相应接口）；同时对方法执行前后的参数上下文进行隔离（可能入参被修改，或者上下文加入了新的参数）：
- `{#xxx#}` 方法执行前的SpEl变量
- `{@xxx@}` 方法执行前的自定义方法
- `[#xxx#]` 方法执行后的SpEl变量
- `[@xxx@]` 方法执行后的函数变量

```java
@ELog(successTemplate = "新建用户 方法执行前用户名:{#user.username#}" 生日:{@DateParseFunction({#user.birthday#})@} 方法执行后用户新建的id:[#user.id#] 方法执行结果:[#result.success#])
public Result addUser(User user) {}
```
```java
public class DateParseFunction implements ILogParseFunction {
    @Override
    public String functionName() {
        return "DateParseFunction";
    }

    @Override
    public Object parse(Object[] args) {
        if (args != null && args.length > 0) {
            if (args[0] != null && args[0] instanceof Date) {
                Date obj = (Date) args[0];
                return DateUtil.formatDateDefault(obj);
            }
        }
        return null;
    }
}
```
> [!IMPORTANT]
> - 自定义方法支持多个参数，参数支持单层嵌套SPEL变量表达式，多个参数逗号分隔
> - 返回值引用标识符默认result

> [!TIP]
> Log注解支持类级别，类注解的模板将作为方法注解的前缀拼接；注解中非模板属性将作为公共配置，可由方法注解覆盖

### 1.3 自定义结果处理器
默认对于方法执行结果不做任何处理，即：方法执行成功与否根据方法是否抛出异常判断；

如：对于带有统一异常处理的spring controller进行处理

可以自定义结果处理器，实现方法执行成功与否的判断（需要实现指定接口，并注册为spring bean）
```java
@ELog(successTemplate = "新建用户", processor = ControllerResultPostProcessor.class)
public void addUser(){}
```
```java
public interface ResultPostProcessor {
    Object process(Object result, MethodExecuteResult methodExecuteResult);
}
```
> [!IMPORTANT]
> 内置的ControllerResultPostProcessor使用需要返回值实现Result接口
> ```java
> public interface Result extends Serializable {
>     boolean success();
> }
> ```

### 1.4 自定义日志执行器
edelweiss-logging本身不负责日志的具体实现，只负责模板解析和业务代码的结合，用户可以自己选择具体的日志实现，将其包装为执行器（实现指定接口）即可
```java
@ELog(successTemplate = "新建用户", executors = {
        @ELogExecutor(clazz = ConsoleLogExecutor.class, async = true),
        @ELogExecutor(clazz = DBLogExecutor.class, async = true)})
public void addUser(){}
```
```java
public interface LogExecutor {
    Object execute(LogPO logPO);
}
```
可以指定多个executor，会按照注解中的顺序依次执行，可以配置是否异步执行，如果配置为异步，会使用内置的线程池执行

内置的线程池可以通过配置文件进行参数配置（如核心线程数，最大线程数等），也可以提供自定义的线程池（注册为spring bean，并命名为"edelweiss.log.thread.pool.default"）

### 1.5 配置
edelweiss-logging在spring配置文件中进行配置，支持全局的日志开关，执行器，业务类型，结果处理器，标签，结果标识，内置线程池配置。
```yaml
edelweiss:
  log:
    prop:
      enable: true
      biz-type:
        global: user-service
      tag:
        global:
          tag1: val1
          tag2: val2
      result-name:
        global: result
      executor:
        global:
          clazz: org.edelweiss.logging.aspect.executor.ConsoleLogExecutor
          async: true
      processor:
        global:
          clazz: org.edelweiss.logging.aspect.processor.ControllerResultPostProcessor
      thread-pool:
        enable: true
        core-pool-size: 8
        maximum-pool-size: 16
        keep-alive-time: 30
        time-unit: minutes
        work-queue-size: 10000
        handler: java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
```
### 1.6 参数上下文共享与隔离
支持方法层级调用下的上下文共享

edelweiss-logging为每个调用链维护了一个栈，栈底有一个默认LogContext，负责存储所有需要共享的参数变量等

每次进入依次切面，就会压入一个新的LogContext，负责存储当前切面/方法的参数上下文，当移出切面后，该LogContext会出栈

![未命名文件(4)](https://github.com/Amuro-R/edelweiss-logging/assets/151483148/01cfbb39-e59e-45a6-9220-87a0eaf598bd)

### 1.7 其他
#### 1.7.1 快速引入
基于springboot自动配置

## 2. TODO
1. 与Spring的集成分离，不再直接依赖Spring
2. 支持非web环境
3. 模板表达式多级嵌套




















