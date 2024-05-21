# roots

[![Coverage Status](https://coveralls.io/repos/github/ranapat/roots/badge.svg?branch=main)](https://coveralls.io/github/ranapat/roots?branch=main)

Roots, bloody roots ...

## Idea

Rx based data flow.

More or less it is

```
(1) Api >> (2) Cache >> (3) Consume
```

with every step optional and interchangeable.

## Simple example

Get or Cache locally

```kotlin
@JsonIgnoreProperties(ignoreUnknown = true)
private class ApiResponse(
    @JsonProperty("status") val status: String,
    @JsonProperty("response") val response: String
)

val flow = TimedGet(
    "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/1",
    ApiResponse::class.java,
    TimeUnit.MINUTES.toMillis(1)
).flow

flow
    .subscribe({ data ->
        Timber.d("... ${data.status} ${data.response}")
    }) { throwable ->
        Timber.e("... $throwable")
    }
```

### More documentation coming up later ...

## Resources

### https://mockapi.io

Project link: https://mockapi.io/projects/663a13b71ae792804bedf83d