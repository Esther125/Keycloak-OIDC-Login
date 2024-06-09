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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.json.JSONObject;


@Service
public class OIDCService {
    private static final Logger log = LoggerFactory.getLogger(OIDCService.class);

    @Value("${oauth2.client-id}")
    private String clientId;

    @Value("${oauth2.client-secret}")
    private String clientSecret;

    @Value("${oauth2.server-url}")
    private String serverUrl;

    @Value("${root.url}")
    private String rootUrl;


    public ResponseEntity<String> exchangeToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", authorizationCode);
        map.add("redirect_uri", rootUrl + "/oauth2/callback");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String tokenEndpoint = serverUrl + "/protocol/openid-connect/token";
        log.info("POST request to tokenEndpoint: " + tokenEndpoint);

        return restTemplate.postForEntity(tokenEndpoint, request, String.class);
    }

    public String parseAccessToken(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.getString("access_token");
    }

    public ResponseEntity<String> fetchUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String userinfoUrl = serverUrl + "/protocol/openid-connect/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(userinfoUrl, HttpMethod.GET, entity, String.class);
        log.info("User Info: " + response.getBody());
        return response;
    }
}



