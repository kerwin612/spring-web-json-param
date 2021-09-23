# spring-web-json-param
  **Allow users to freely use json parameters in spring-web projects.**  
  **support `spring-webmvc` and `spring-webflux`**  
  

**maven**:  
```xml
<dependency>
  <groupId>io.github.kerwin612</groupId>
  <artifactId>spring-web-json-param</artifactId>
  <version>1.0</version>
</dependency>
```  
**gradle**:    
```groovy
implementation 'io.github.kerwin612:spring-web-json-param:1.0'
```   

**enable**:   
```java
@EnableJsonParam
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```  


**example**:  
```java
@PostMapping("/post1")
Mono<String> post1(@JsonParam String param1, @JsonParam String param2) {
}  
    
@PostMapping("/post2")
Mono<String> post2(@JsonParam("key1") String param1, @JsonParam("key2") String param2) {
}
```
