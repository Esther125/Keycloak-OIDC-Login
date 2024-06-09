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
package com.kazuki43zoo.jpetstore.ui.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.HttpEntity;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import com.kazuki43zoo.jpetstore.service.OIDCService;



@Controller
public class OIDCAuthController {
    private static final Logger log = LoggerFactory.getLogger(OIDCAuthController.class);
    @Value("${oauth2.client-id}")
    private String clientId;

    @Value("${oauth2.client-secret}")
    private String clientSecret;

    @Value("${oauth2.server-url}")
    private String serverUrl;

    @Value("${oauth2.auth-endpoint}")
    private String authEndpoint;

    @Value("${root.url}")
    private String rootUrl;

    @Autowired
    private OIDCService oidcService;

    // Request for Authorization Code
    @GetMapping("/login-oidc")
    public ModelAndView redirectToKeycloak() {
        String redirectUri = rootUrl + "/oauth2/callback"; // The URI where Keycloak redirects back to client app
        String responseType = "code"; // Using Authorization Code Flow
        String scope = "openid";

        // Construct the full authorization URL
        String authorizationUrl = String.format(
                "%s%s?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s",
                serverUrl, authEndpoint, clientId, redirectUri, responseType, scope
        );

        log.info("Redirecting to Keycloak Authorization URL: {}", authorizationUrl);

        // Redirect to the Keycloak login page
        return new ModelAndView("redirect:" + authorizationUrl);
    }

    // Request to token endpoint in Keyclaok
    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> handleAuthorizationCode(@RequestParam("code") String authorizationCode) {
        ResponseEntity<String> response = oidcService.exchangeToken(authorizationCode);
        if (response.getStatusCode()== HttpStatus.OK) {
            String body = response.getBody();
            log.info("BODY CONTENT: "+body);

            // temp: redirect back to application
            HttpHeaders redirectHeaders = new HttpHeaders();
            redirectHeaders.setLocation(URI.create(rootUrl + "/catalog"));
            return new ResponseEntity<>(redirectHeaders, HttpStatus.SEE_OTHER);

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve access token");
        }
    }
}

