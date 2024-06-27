package com.example.pausematefrontend

import android.os.Bundle
import android.util.Log
import okhttp3.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.IOException
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pausematefrontend.ui.theme.PauseMateFrontendTheme
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : ComponentActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton: Button = findViewById(R.id.playButton)
        val pauseButton: Button = findViewById(R.id.pauseButton)
        val serverIpEditText: EditText = findViewById(R.id.serverIpEditText)

        playButton.setOnClickListener {
            val serverIpAddress = serverIpEditText.text.toString()
            println("serverIpAddress $serverIpAddress")
            if (serverIpAddress.isNotEmpty()) {
                Log.println(Log.INFO, "play","serverIpAddress $serverIpAddress")
                sendPostRequest("http://$serverIpAddress:8080/api/control","play")
            }
        }

        pauseButton.setOnClickListener {
            val serverIpAddress = serverIpEditText.text.toString()

            println("serverIpAddress $serverIpAddress")
            if (serverIpAddress.isNotEmpty()) {
                Log.println(Log.INFO, "pause","serverIpAddress $serverIpAddress")
                sendPostRequest("http://$serverIpAddress:8080/api/control","pause")
            }
        }
    }

    private fun sendPostRequest(url: String, action: String) {
        val json = """
            {
                "action": "$action"
            }
        """.trimIndent()
        val request = Request.Builder()
            .url(url)
            .post(json.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.body?.string())
            }
        })
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
    PauseMateFrontendTheme {
        Greeting("Android")
    }
}