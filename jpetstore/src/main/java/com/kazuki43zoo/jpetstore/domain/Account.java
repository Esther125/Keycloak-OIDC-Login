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
package com.kazuki43zoo.jpetstore.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;

/**
 * @author Kazuki Shimizu
 */
@Getter
@Setter
public class Account implements Serializable {

	private static final long serialVersionUID = 8751282105532159742L;

	private String username;
	private String password = "password";
	private String email = "default@gmail.com";
	private String firstName = "default";
	private String lastName = "default";
	private String status = "default";
	private String address1= "default";
	private String address2= "default";
	private String city= "default";
	private String state= "default";
	private String zip= "999";
	private String country= "default";
	private String phone= "999-9999-9999";
	private String favouriteCategoryId= "default";
	private String languagePreference = "en";
	private boolean listOption = FALSE;
	private boolean bannerOption = FALSE;
	private String bannerName = "default";

}
