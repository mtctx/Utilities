package dev.mtctx.utilities.ktor

import dev.mtctx.utilities.datasizes.kib
import dev.mtctx.utilities.io.absolutePathString
import dev.mtctx.utilities.io.fileSystem
import dev.mtctx.utilities.io.temp
import dev.mtctx.utilities.outcome.Outcome
import dev.mtctx.utilities.outcome.failure
import dev.mtctx.utilities.outcome.success
import dev.mtctx.utilities.serialization.jsonForMachines
import dev.mtctx.utilities.serialization.modify
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Path
import okio.buffer
import java.io.IOException
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object Ktor {
    private var _client = createNewClient()

    fun createNewClient() = HttpClient(CIO) {
        expectSuccess = true

        install(HttpTimeout) {
            socketTimeoutMillis = 30.seconds.inWholeMilliseconds
            connectTimeoutMillis = 10.seconds.inWholeMilliseconds
            requestTimeoutMillis = 2.minutes.inWholeMilliseconds
        }

        install(HttpRequestRetry) {
            maxRetries = 3
            retryIf { _, response -> !response.status.isSuccess() }
            retryOnExceptionIf { _, cause -> cause is IOException }
            delayMillis { retry -> retry * 2000L }
        }

        install(ContentNegotiation) {
            json(jsonForMachines.modify { ignoreUnknownKeys = true; isLenient = true })
        }
    }

    fun resetClient() {
        _client.close()
        _client = createNewClient()
    }

    val client: HttpClient get() = _client

    enum class DownloadStage {
        RECEIVING_BYTES,
        WRITING_TO_FILE,
        DONE
    }

    suspend fun HttpClient.downloadResumable(
        url: String,
        destination: Path,
        bufferSize: Long = 8.kib,
        onProgress: (stage: DownloadStage, bytesWritten: Long, totalBytes: Long, elapsed: Duration) -> Unit = { _, _, _, _ -> }
    ): Outcome<Boolean> {
        val tmpDestination = destination.temp(".${Clock.System.now().toEpochMilliseconds()}.tmp")
        val destinationAbsoluteString = destination.absolutePathString()

        if (fileSystem.metadataOrNull(destination)?.isDirectory == true) {
            return failure("The destination $destinationAbsoluteString is not a file!")
        }

        // Get the current file size using Okio
        val startByte = if (fileSystem.exists(tmpDestination)) {
            fileSystem.metadata(tmpDestination).size ?: 0L
        } else 0L

        val startTime = Clock.System.now()
        return try {
            prepareGet(url) {
                if (startByte > 0) {
                    header(HttpHeaders.Range, "bytes=$startByte-")
                }
            }.execute { response ->
                // Handle "Already Finished" or "Invalid Range"
                if (response.status == HttpStatusCode.RequestedRangeNotSatisfiable) {
                    val total = fileSystem.metadataOrNull(tmpDestination)?.size ?: startByte
                    onProgress(DownloadStage.DONE, total, total, Clock.System.now() - startTime)
                    fileSystem.atomicMove(tmpDestination, destination)
                    return@execute success()
                }

                // Determine if we are resuming or starting fresh
                val isResuming = response.status == HttpStatusCode.PartialContent

                // If resuming, Content-Length is the REMAINING bytes.
                // We add startByte to get the actual full file size.
                val contentLength = response.contentLength() ?: -1L
                val totalBytes = if (isResuming) contentLength + startByte else contentLength

                var currentBytes = if (isResuming) startByte else 0L

                // Open Sink
                val sink = withContext(Dispatchers.IO) {
                    if (isResuming) fileSystem.appendingSink(tmpDestination)
                    else fileSystem.sink(tmpDestination)
                }.buffer()

                try {
                    val channel = response.bodyAsChannel()
                    val buffer = ByteArray(bufferSize.toInt())

                    while (!channel.isClosedForRead) {
                        val bytesRead = channel.readAvailable(buffer)
                        if (bytesRead <= 0) break

                        onProgress(
                            DownloadStage.RECEIVING_BYTES,
                            currentBytes,
                            totalBytes,
                            Clock.System.now() - startTime
                        )

                        sink.write(buffer, 0, bytesRead)
                        currentBytes += bytesRead
                        onProgress(
                            DownloadStage.WRITING_TO_FILE,
                            currentBytes,
                            totalBytes,
                            Clock.System.now() - startTime
                        )
                    }
                    fileSystem.atomicMove(tmpDestination, destination)
                    return@execute success()
                } catch (e: Exception) {
                    return@execute failure("Stream interrupted: ${e.message}", e)
                } finally {
                    onProgress(DownloadStage.DONE, currentBytes, totalBytes, Clock.System.now() - startTime)
                    withContext(Dispatchers.IO) { sink.close() }
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            return failure("Connection failed: ${e.message}", e)
        }
    }
}

val ktorClient get() = Ktor.client