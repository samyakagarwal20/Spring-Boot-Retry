# Spring-Boot-Retry
It is a spring boot application to demonstrate the working and implementation of Spring Retry module

---
## Spring Retry

**Spring Retry** is part of larger Spring framework ecosystem which provides support for handling and **recovering from transient failures like network timeout, database connection issue**, etc. in a more robust and controlled manner.

To enable retry functionality in spring boot, the first step is to add ```@EnableRetry``` annotation to either the ```main class``` or any of the ```@Configuration``` class.

## Approach Used: @Retryable annotation

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
