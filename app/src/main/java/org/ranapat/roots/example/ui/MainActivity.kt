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
import org.ranapat.roots.api.Get
import org.ranapat.roots.api.body
import org.ranapat.roots.api.instance
import org.ranapat.roots.converter.Converter
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
    private val roots:List<Maybe<ApiResponse>> by lazy {
        listOf(
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/1")
                .body()
                .map { body ->
                    Converter.from(body)
                },
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/2")
                .body()
                .instance(ApiResponse::class.java),
            Get
                .from("https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/2")
                .instance(ApiResponse::class.java)
        )
    }
        /*Get.json(
            "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
            ApiResponse::class.java,
            normaliseResponse = object: NormaliseResponse<ApiResponse> {
                override fun invoke(response: Response): ApiResponse {
                    return toTyped(
                        JSONArray(response.body?.string()).getJSONObject(0).toString(),
                        ApiResponse::class.java
                    )
                }
            }
        )
    }
    private val getApiResponse1 by lazy {
        Get.json(
            "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/1",
            ApiResponse::class.java
        )
    }
    private val getApiResponse2 by lazy {
        Get.json(
            "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response/2"
        ).like<ApiResponse>()
    }

    private val postApiResponse1 by lazy {
        Post.json(
            "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
            "{\"status\":\"predefined status (3)\", \"response\": \"predefined response (3)\"}",
            ApiResponse::class.java,
            "application/json; charset=utf-8".toMediaType()
        )
    }
    private val postApiResponse2 by lazy {
        Post.json(
            "https://663a13b71ae792804bedf83c.mockapi.io/api/v1/response",
            "{\"status\":\"predefined status (4)\", \"response\": \"predefined response (4)\"}",
            "application/json; charset=utf-8".toMediaType()
        ).like(ApiResponse::class.java)
    }*/

    override val compositeDisposable
        get() = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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