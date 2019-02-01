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
    @ApiThrottled(bucket="throttle-policy-bucket", blocking = WAITING)
    @ApiThrottled(bucket="another-throttle-policy-bucket")
    public void someApiMethod() {
        // requesting the api...
    }
}
```

* Defint the policy for each bucket
* Add/Remove token for each bucket

## Blocking Policy
* Wait: keep waiting until get new token
* Reject: deny the requst

## Bucket Policy

* Support to make at most `N` requests within a windowed interval `I`(Rolling Window)
* Support to make `N` request in total
* Support minimum delay `D` between requests.

* A Combination of Above Policies

```java
public class BucketPolicy {
    /**
    * The window size in second to be measured.
    * -1 means infinite window size will be applied
    */
    private int window;
    /**
    * Number of tokens that are available for {@link #window}.
    * -1 means no limitation on the number of the tokens
    */
    private int nToken;
    /**
    * Minimum interval between requests
    * 0 means no interval required between requests
    */
    private int interval;
}
```

## Reference
1. https://zhuanlan.zhihu.com/p/20872901
1. https://en.wikipedia.org/wiki/Token_bucket
1. https://engineering.classdojo.com/blog/2015/02/06/rolling-rate-limiter/
