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

import com.kazuki43zoo.jpetstore.domain.Account;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

/**
 * @author Kazuki Shimizu
 */
// ORIGINAL CODE

//@Getter
//@EqualsAndHashCode(callSuper = false)
//public class AccountUserDetails extends User {
//
//	private static final long serialVersionUID = -3065955491112229927L;
//	private final Account account;
//
//	public AccountUserDetails(Account account) {
//		super(account.getUsername(), account.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
//		this.account = account;
//	}
//
//}

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

public class AccountUserDetails extends User implements OidcUser {
	private static final Logger logger = Logger.getLogger(AccountUserDetails.class.getName());

	private final OidcUser oidcUser;

	@Getter
	private final Account account;

	public AccountUserDetails(OidcUser oidcUser) {
		super(oidcUser.getName(), "", oidcUser.getAuthorities()); // already authenticated so no need of password
		this.oidcUser = oidcUser;
		this.account = new Account();

		// set Account attributes
		this.account.setUsername((String) oidcUser.getAttributes().get("preferred_username"));
		this.account.setFirstName((String) oidcUser.getAttributes().get("given_name"));
		this.account.setLastName((String) oidcUser.getAttributes().get("family_name"));
		this.account.setEmail((String) oidcUser.getAttributes().get("email"));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oidcUser.getAttributes();
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return (Collection<GrantedAuthority>) oidcUser.getAuthorities();
	}

	@Override
	public String getName() {
		return oidcUser.getName();
	}

	@Override
	public Map<String, Object> getClaims() {
		return oidcUser.getClaims();
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return oidcUser.getUserInfo();
	}

	@Override
	public OidcIdToken getIdToken() {
		return oidcUser.getIdToken();
	}
}
