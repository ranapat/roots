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
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.ranapat.roots.api.getFromJson
import org.ranapat.roots.example.ui.theme.RootsTheme
import org.ranapat.roots.tools.Dispenser
import timber.log.Timber

class MainActivity : ComponentActivity(), Dispenser {
    private val getApiResponse by lazy { getFromJson("https://pit.ranapat.org/drop/get.json", ApiResponse::class.java) }

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
                                    Timber.d("button is clicked")
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
        subscription(
            getApiResponse
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                try {
                    Timber.d(data.status + " " + data.response)
                } catch (e: JsonProcessingException) {
                    Timber.e(e)
                } catch (e: JsonMappingException) {
                    Timber.e(e)
                } catch (e: Throwable) {
                    Timber.e(e)
                }
            }) { throwable ->
                Timber.e(throwable)
            }
        )
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

class ApiResponse(
    @JsonProperty("status") val status: String,
    @JsonProperty("response") val response: String
)