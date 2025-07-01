package com.example.findyourself.repositories

import android.util.Log
import com.example.findyourself.dataClasses.ConnectResponse
import com.example.findyourself.dataClasses.User
import com.example.findyourself.retrofit.API
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume


class ConnectRepository: KoinComponent {
    private val api: API by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun connectToStranger(userId: String, interests: List<String>): Result<ConnectResponse> {
        return try {
            val client = OkHttpClient()
            val interestParam = interests.joinToString(",")
            val url = "ws://192.168.29.81:8080/connect?uid=$userId&interests=$interestParam"
            val request = Request.Builder().url(url).build()

            return suspendCancellableCoroutine { continuation ->
                var pingJob: Job? = null
                val resumed = AtomicBoolean(false)

                val listener = object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        pingJob = CoroutineScope(Dispatchers.IO).launch {
                            while (isActive) {
                                webSocket.send("ping")
                                Log.d("Jaipur99", "Sending Ping Again")
                                delay(3000L)
                            }
                        }
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        if (text.contains("timeout")) {
                            Log.d("Jaipur99", "Server timed out, closing socket.")
                            pingJob?.cancel()
                            webSocket.close(1000, "Timeout reached")
                            if (resumed.compareAndSet(false, true)) {
                                continuation.resume(Result.failure(Exception("Server timeout")))
                            }
                            return
                        }

                        try {
                            val match = Json.decodeFromString<ConnectResponse>(text)
                            pingJob?.cancel()
                            if (resumed.compareAndSet(false, true)) {
                                Log.d("Jaipur99", "Success, Connection Closing")
                                webSocket.close(1000, null)
                                continuation.resume(Result.success(match))
                            }
                        } catch (e: Exception) {
                            pingJob?.cancel()
                            if (resumed.compareAndSet(false, true)) {
                                Log.d("Jaipur99", "Failure, Connection Closing")
                                webSocket.close(1001, "Invalid message format")
                                continuation.resume(Result.failure(e))
                            }
                        }
                    }


                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        Log.e("Jaipur99", "Failure: ${t.message}", t)
                        pingJob?.cancel()
                        if (resumed.compareAndSet(false, true)) {
                            continuation.resume(Result.failure(t)) {
                                webSocket.close(1001, null)
                            }
                        }
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        pingJob?.cancel()
                        webSocket.close(code, reason)
                        Log.d("Jaipur99","Connection Closing")
                    }
                }

                val ws = client.newWebSocket(request, listener)

                continuation.invokeOnCancellation {
                    pingJob?.cancel()
                    ws.close(1000, "Cancelled by client")
                }
            }
        } catch (e: Exception) {
            Log.d("Jaipur99", "Error In Websocket : $e")
            Result.failure(e)
        }
    }

    suspend fun getChatUser(userId : String) : Result<User>{
        return try {
            val response = api.getChatUser(userId)
            Log.d("ConnectJaipur",  "Response :  ${response.body()}")
            if(response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Failed to get ChatUser Details"))
            }

        }catch (e : Exception){
            Log.d("ConnectJaipur",  "Exception getting chat user:  ${e.message}")
            Result.failure(e)
        }
    }
}