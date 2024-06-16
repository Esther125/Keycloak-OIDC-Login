/*
 *    Copyright 2016-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.kazuki43zoo.jpetstore.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class JpetstoreHealthIndicator extends AbstractHealthIndicator {

    private static final String EXTERNAL_SERVICE_URL = "http://localhost:8080"; 

    private final HttpClient httpClient;

    public JpetstoreHealthIndicator() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10)) //設置連線超時時間
                .build();
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(EXTERNAL_SERVICE_URL))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                builder.up()
                        .withDetail("External_Service", "Service is Up and Running ✅")
                        .withDetail("url", EXTERNAL_SERVICE_URL);
            } else if (statusCode >= 400 && statusCode < 500) {
                builder.down()
                        .withDetail("External_Service", "Client Error: " + statusCode)
                        .withDetail("url", EXTERNAL_SERVICE_URL);
            } else if (statusCode >= 500 && statusCode < 600) {
                builder.down()
                        .withDetail("External_Service", "Server Error: " + statusCode)
                        .withDetail("url", EXTERNAL_SERVICE_URL);
            } else {
                builder.unknown()
                        .withDetail("External_Service", "Unknown status: " + statusCode)
                        .withDetail("url", EXTERNAL_SERVICE_URL);
            }
        } catch (IOException | InterruptedException e) {
            builder.down()
                    .withDetail("External_Service", "Service is Down 🔻")
                    .withDetail("error_message", e.getMessage())
                    .withDetail("url", EXTERNAL_SERVICE_URL);
        }
    }
}
