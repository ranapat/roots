package org.ranapat.roots.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import org.ranapat.roots.api.Get
import org.ranapat.roots.api.NormaliseResponse
import org.ranapat.roots.api.Post
import org.ranapat.roots.api.instance
import org.ranapat.roots.api.jsonArray
import org.ranapat.roots.api.jsonObject
import org.ranapat.roots.api.string
import org.ranapat.roots.cache.CacheConfig
import org.ranapat.roots.cache.Config
import org.ranapat.roots.cache.cache
import org.ranapat.roots.cache.instance
import org.ranapat.roots.converter.fromJson
import org.ranapat.roots.converter.instance
import org.ranapat.roots.example.ui.theme.RootsTheme
import org.ranapat.roots.tools.Dispenser
import timber.log.Timber

class MainActivity : ComponentActivity(), Dispenser {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    override val compositeDisposable
        get() = CompositeDisposable()

    private val roots:List<Maybe<ApiResponse>> by lazy {
        listOf(
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/1")
                .string()
                .map { body ->
                    fromJson(body)
                },
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/2")
                .string()
                .instance(ApiResponse::class.java),
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/2")
                .instance(ApiResponse::class.java),
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response")
                .instance(object : NormaliseResponse<ApiResponse> {
                    override fun invoke(from: String): ApiResponse {
                        val json = JSONArray(from)
                        return fromJson<ApiResponse>(json.getJSONObject(0).toString())
                    }
                }),
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response")
                .jsonArray()
                .map { json ->
                    json.getJSONObject(0).toString()
                }
                .map { jsonString ->
                    fromJson<ApiResponse>(jsonString)
                },

            Post
                .from(
                    "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
                    "{\"status\":\"predefined status (3.1)\", \"response\": \"predefined response (3.1)\"}",
                    "application/json; charset=utf-8".toMediaType()
                )
                .string()
                .map { body ->
                    fromJson(body)
                },
            Post
                .from(
                    "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
                    "{\"status\":\"predefined status (3.2)\", \"response\": \"predefined response (3.2)\"}",
                    "application/json; charset=utf-8".toMediaType()
                )
                .string()
                .instance(ApiResponse::class.java),
            Post
                .from(
                    "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
                    "{\"status\":\"predefined status (3.3)\", \"response\": \"predefined response (3.3)\"}",
                    "application/json; charset=utf-8".toMediaType()
                )
                .instance(ApiResponse::class.java),
            Post
                .from(
                    "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
                    "{\"status\":\"predefined status (3.4)\", \"response\": \"predefined response (3.4)\"}",
                    "application/json; charset=utf-8".toMediaType()
                )
                .instance(object : NormaliseResponse<ApiResponse> {
                    override fun invoke(from: String): ApiResponse {
                        val json = JSONObject(from)
                        return fromJson<ApiResponse>(json.toString())
                    }
                }),
            Post
                .from(
                    "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
                    "{\"status\":\"predefined status (3.5)\", \"response\": \"predefined response (3.5)\"}",
                    "application/json; charset=utf-8".toMediaType()
                )
                .jsonObject()
                .map { json ->
                    json.toString()
                }
                .map { jsonString ->
                    fromJson<ApiResponse>(jsonString)
                },

            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/2")
                .string()
                .cache("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/2")
                .instance(ApiResponse::class.java)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CacheConfig.config = Config(
            baseContext,
            pathStructure = CacheConfig.PathStructure.NESTED
        )

        setContent {
            RootsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxHeight()
                            .fillMaxHeight()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()

                        ) {
                            Button(
                                onClick = {
                                    example()
                                }) {
                                Text(text = "Click me")
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dispose()
    }

    private fun example() {
        roots.forEachIndexed { index, root ->
            subscription(
                root
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ data ->
                        try {
                            Timber.d("Root${index} :: ${data.status} ${data.response}")
                        } catch (e: JsonProcessingException) {
                            Timber.e("Root{$index} $e")
                        } catch (e: JsonMappingException) {
                            Timber.e("Root{$index} $e")
                        } catch (e: Throwable) {
                            Timber.e("Root{$index} $e")
                        }
                    }) { throwable ->
                        Timber.e("Root{$index} $throwable")
                    }
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RootsTheme {
        Greeting("Android")
    }
}