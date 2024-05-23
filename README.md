# roots

[![Coverage Status](https://coveralls.io/repos/github/ranapat/roots/badge.svg?branch=main&release=latest)](https://coveralls.io/github/ranapat/roots?branch=main)

Roots, bloody roots ...

## Idea

Rx based data flow.

More or less it is

```
(1) Api >> (2) Cache >> (3) Consume >> (4) Something else
```

with every step optional and interchangeable.

## How to get it

### Get latest from jitpack
[![](https://jitpack.io/v/ranapat/roots.svg)](https://jitpack.io/#ranapat/roots)

### Browser all from jitpack
Access all jitpack builds [here](https://jitpack.io/#ranapat/roots).

### Check the releases
All releases are available [here](https://github.com/ranapat/roots/tags).

### Get it from source
Check out the repo and have fun.

## Requirements
* Java 17
* Android SDK
* Gradle

## Building
Build tool is gradle

### Assemble
Run `./gradlew assemble`

### Run unit tests
Run `./gradlew test`

### Run lint
Run `./gradlew lint`

### Run jacoco tests
Run `./gradlew testDebugUnitTestCoverage` or `./gradlew testReleaseUnitTestCoverage` or `./scripts/tests`

### Outputs
You can find the outputs here:
- for the lint
  `./roots/build/reports/lint-results-developmentDebug.html`
- for the unit test coverage
  `./roots/build/reports/jacoco/testDebugUnitTestCoverage/html/index.html` or `./roots/build/reports/jacoco/testReleaseUnitTestCoverage/html/index.html`
- for the unit test summary
  `./roots/build/reports/tests/testDebugUnitTestCoverage/index.html` or `./roots/build/reports/tests/testReleaseUnitTestCoverage/index.html`

## Join the project
If you find this project interesting check out the ongoing
[issues](https://github.com/ranapat/roots/issues) and add your ides.

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

This will:

- get from url and cache it locally
- keep it for 1 minute
- within 1 minute if requested again get it locally
- after 1 minute re-get it again (and cache it again)

### More documentation coming up soon ...

## Resources

### https://mockapi.io

Project link: https://mockapi.io/projects/663a13b71ae792804bedf83d