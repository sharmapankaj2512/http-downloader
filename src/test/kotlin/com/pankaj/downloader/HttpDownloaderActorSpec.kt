package com.pankaj.downloader

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.kotlintest.specs.StringSpec
import io.mockk.*
import io.vlingo.actors.Definition
import io.vlingo.actors.World
import java.net.URL


class HttpDownloaderActorSpec : StringSpec({
    // FOR NOW ASSUME THAT DOWNLOADER SAVES TO DISK FUTURE ADD AN ACTOR FOR IT
    val world = World.startWithDefaults("http-downloader")
    val tracker = mockk<ProgressTracker>()
    val writer = mockk<StreamWriter>()
    val url = URL("http://test.com/test.mp3")
    val definition = Definition.has(HttpDownloaderActor::class.java, Definition.parameters(url, writer, tracker))
    val httpDownloader = world.actorFor(definition, HttpDownloader::class.java)



    "test something" {
        //GIVEN
        WireMock.configureFor(8089)
        val wireMockServer = WireMockServer(8089)
        wireMockServer.start()

        every { tracker.upperBound(any()) } just Runs
        every { tracker.increaseBy(any()) } just Runs
        every { writer.append(any()) } just Runs
        every { writer.flush() } just Runs

        stubFor(
            get(urlEqualTo(url.toString()))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "octet/stream")
                        .withBody("Hello world!".toByteArray())
                )
        )

        httpDownloader.startDownload()

        Thread.sleep(20000)

        verify { tracker.upperBound(any()) }
        verify { tracker.increaseBy(any()) }

        wireMockServer.stop()
    }

})