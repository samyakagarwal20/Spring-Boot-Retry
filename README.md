# Spring-Boot-Retry
It is a spring boot application to demonstrate the working and implementation of Spring Retry module

---
## Spring Retry

**Spring Retry** is part of larger Spring framework ecosystem which provides support for handling and **recovering from transient failures like network timeout, database connection issue**, etc. in a more robust and controlled manner.

To enable retry functionality in spring boot, the first step is to add ```@EnableRetry``` annotation to either the ```main class``` or any of the ```@Configuration``` class.

## Approach 1: @Retryable annotation

The most simple way to enable retry mechanism is by using the ```@Retryable``` annotation with the ```service class method implementing the business logic``` or we can also use it at the ```service interface methods```.

```
@Service
public class UserServiceImpl implements UserService {
    @Override
    @Retryable
    public List<User> getAllUsers() {
        // code
    }
}
```


```
public interface UserService {
    @Retryable
    List<User> getAllUsers();
}
```

The following are the different attributes that can be used with this annotation:
* ```retryFor``` - list the exception types that are retryable.
* ```noRetryFor``` - list the exception types that are not retryable.
* ```label``` - a label that can be used for logging and monitoring the retry activity
* ```maxAttempt``` - the maximum number of attempts (including the first failure), defaults to 3
* ```backoff``` - specifies the backoff policy to use between retry attempts. You can customize the delay between retries using various properties such as delay, maxDelay, and multiplier.
* ```recover``` - specifies a method that will be called when a retryable exception is encountered. This method can be used to perform cleanup or recovery actions, such as closing a database connection or rolling back a transaction.
* ```stateful``` - Flag to say that the retry is stateful: i.e. exceptions are re-thrown. If false then retryable exceptions are not re-thrown.

For example, 
```
@Retryable(label = "retry-getAllUsers()", maxAttempts = 4, backoff = @Backoff(delay = 2000), retryFor = {IOException.class}, noRetryFor = {SQLException.class})
```

---

## Approach 2: @Retryable annotation with Exception Handling

This approach is simply an extension of the approach 1.

Spring Rest **@Retryable works differently with exception handling**. With this, we mean that if **we are catching and handling the exception, then retry is disabled**. This is because **retry only works when a method throws an exception**.

```
@Retryable(label = "retry-getAllUsers()", maxAttempts = 4, backoff = @Backoff(delay = 2000), retryFor = {IOException.class}, noRetryFor = {SQLException.class})
public List<User> getAllUsers() {
    try{
        // code
    } catch (Exception e) {
        LOGGER.error("Error in getAllUsers() by {} : {}", e.getClass().getCanonicalName(), e.getMessage());
    }
}
```

We can still have a workaround to this approach by simply throwing again the exception from inside the catch block. The reason is that when exception is thrown for the 1st time inside the try block, it will be intercepted by the catch block rather than the @Retryable annotation.

But when we again throw the same exception from inside the catch block, since there are no nested try-catch block available, so this time the request will be intercepted by the @Retryable annotation.
```
@Retryable(label = "retry-getAllUsers()", maxAttempts = 4, backoff = @Backoff(delay = 2000), retryFor = {IOException.class}, noRetryFor = {SQLException.class})
public List<User> getAllUsers() {
    try{
        // code
    } catch (SQLException e) {
        LOGGER.error("Error in getAllUsers() by {} : {}", e.getClass().getCanonicalName(), e.getMessage());
        throw new SQLException(e.getMessage());
    } 
    catch (Exception e) {
        LOGGER.error("Error in getAllUsers() by {} : {}", e.getClass().getCanonicalName(), e.getMessage());
        throw new Exception(e.getMessage());
    }
}
```


