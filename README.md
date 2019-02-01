# API Throttle
An Api Throttle using token bucket algorithm implemented with Spring Boot and Redis.

## Usage

* Enable the throttle function
```java
@EnableApiThrottle
@Configuration
public class SomeConfiguration {    
}
```

* Add throttle to the target method
```java
@Component
public class SomeApiClass {
    @ApiThrottled(bucket="throttle-policy-bucket")
    @ApiThrottled(bucket="another-throttle-policy-bucket")
    public void someApiMethod() {
        // requesting the api...
    }    
}
```

* Defint the policy for each bucket
* Add/Remove token for each bucket

## Supported Policy

* Support to make at most `N` requests within a windowed interval `I`(Rolling Window)
* Support to make `N` request in total
* Support minimum delay `D` between requests.

* A Combination of Above Policies

## Reference
1. https://zhuanlan.zhihu.com/p/20872901
1. https://en.wikipedia.org/wiki/Token_bucket
1. https://engineering.classdojo.com/blog/2015/02/06/rolling-rate-limiter/
