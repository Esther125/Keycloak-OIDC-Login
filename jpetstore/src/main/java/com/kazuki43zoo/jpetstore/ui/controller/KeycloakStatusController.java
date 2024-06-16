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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@RestController
public class KeycloakStatusController {

    @GetMapping("/keycloak_status")
    public String getKeycloakStatus() {
        try {
            // 打印当前工作目录
            String currentDir = System.getProperty("user.dir");
            //System.out.println("Current working directory: " + currentDir);

            // 使用相对路径
            Path path = Paths.get(currentDir, "../keycloak_status.txt");
            //System.out.println("Attempting to read file: " + path.toAbsolutePath());
            if (Files.exists(path)) {
                if (Files.isReadable(path)) {
                    String content = new String(Files.readAllBytes(path)).trim();
                    System.out.println("File content: " + content);
                    return content;
                } else {
                    System.err.println("File is not readable: " + path.toAbsolutePath());
                    return "UNKNOWN";
                }
            } else {
                System.err.println("File does not exist: " + path.toAbsolutePath());
                return "UNKNOWN";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "UNKNOWN";
        }
    }
}
