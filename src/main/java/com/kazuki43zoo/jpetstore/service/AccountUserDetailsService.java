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

import com.kazuki43zoo.jpetstore.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.AddressStandardClaim;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;

/**
 * @author Kazuki Shimizu
 */
//@Service
//@RequiredArgsConstructor
//public class AccountUserDetailsService implements UserDetailsService {
//
//	private final AccountMapper accountMapper;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) {
//		return Optional.ofNullable(accountMapper.getAccountByUsername(username))
//				.map(AccountUserDetails::new)
//				.orElseThrow(() -> new UsernameNotFoundException(username));
//	}
//
//}


import com.kazuki43zoo.jpetstore.domain.Account;
import com.kazuki43zoo.jpetstore.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountUserDetailsService implements UserDetailsService {

	private final AccountMapper accountMapper;

	@Override
	public UserDetails loadUserByUsername(String username) {
		return Optional.ofNullable(accountMapper.getAccountByUsername(username))
				.map(this::toAccountUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}

	private AccountUserDetails toAccountUserDetails(Account account) {
		// Change Account into AccountUserDetails
		OidcUser oidcUser = new OidcUserImpl(account);
		return new AccountUserDetails(oidcUser);
	}

	private static class OidcUserImpl implements OidcUser {
		private final Account account;

		public OidcUserImpl(Account account) {
			this.account = account;
		}

		@Override
		public <A> A getAttribute(String name) {
			return OidcUser.super.getAttribute(name);
		}

		@Override
		public Map<String, Object> getAttributes() {
			// Change Account attribute to OIDC attribute
			return Map.of(
					"given_name", account.getFirstName(),
					"family_name", account.getLastName(),
					"email", account.getEmail()
			);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return null;
		}

		@Override
		public String getName() {
			return account.getUsername();
		}

		@Override
		public Map<String, Object> getClaims() {
			return getAttributes();
		}

		@Override
		public <T> T getClaim(String claim) {
			return OidcUser.super.getClaim(claim);
		}

		@Override
		public boolean hasClaim(String claim) {
			return OidcUser.super.hasClaim(claim);
		}

		@Override
		public Boolean containsClaim(String claim) {
			return OidcUser.super.containsClaim(claim);
		}

		@Override
		public String getClaimAsString(String claim) {
			return OidcUser.super.getClaimAsString(claim);
		}

		@Override
		public Boolean getClaimAsBoolean(String claim) {
			return OidcUser.super.getClaimAsBoolean(claim);
		}

		@Override
		public Instant getClaimAsInstant(String claim) {
			return OidcUser.super.getClaimAsInstant(claim);
		}

		@Override
		public URL getClaimAsURL(String claim) {
			return OidcUser.super.getClaimAsURL(claim);
		}

		@Override
		public Map<String, Object> getClaimAsMap(String claim) {
			return OidcUser.super.getClaimAsMap(claim);
		}

		@Override
		public List<String> getClaimAsStringList(String claim) {
			return OidcUser.super.getClaimAsStringList(claim);
		}

		@Override
		public OidcUserInfo getUserInfo() {
			return new OidcUserInfo(getAttributes());
		}

		@Override
		public OidcIdToken getIdToken() {
			return null; // 返回 OIDC ID Token
		}

		@Override
		public URL getIssuer() {
			return OidcUser.super.getIssuer();
		}

		@Override
		public String getSubject() {
			return OidcUser.super.getSubject();
		}

		@Override
		public String getFullName() {
			return OidcUser.super.getFullName();
		}

		@Override
		public String getGivenName() {
			return OidcUser.super.getGivenName();
		}

		@Override
		public String getFamilyName() {
			return OidcUser.super.getFamilyName();
		}

		@Override
		public String getMiddleName() {
			return OidcUser.super.getMiddleName();
		}

		@Override
		public String getNickName() {
			return OidcUser.super.getNickName();
		}

		@Override
		public String getPreferredUsername() {
			return OidcUser.super.getPreferredUsername();
		}

		@Override
		public String getProfile() {
			return OidcUser.super.getProfile();
		}

		@Override
		public String getPicture() {
			return OidcUser.super.getPicture();
		}

		@Override
		public String getWebsite() {
			return OidcUser.super.getWebsite();
		}

		@Override
		public String getEmail() {
			return OidcUser.super.getEmail();
		}

		@Override
		public Boolean getEmailVerified() {
			return OidcUser.super.getEmailVerified();
		}

		@Override
		public String getGender() {
			return OidcUser.super.getGender();
		}

		@Override
		public String getBirthdate() {
			return OidcUser.super.getBirthdate();
		}

		@Override
		public String getZoneInfo() {
			return OidcUser.super.getZoneInfo();
		}

		@Override
		public String getLocale() {
			return OidcUser.super.getLocale();
		}

		@Override
		public String getPhoneNumber() {
			return OidcUser.super.getPhoneNumber();
		}

		@Override
		public Boolean getPhoneNumberVerified() {
			return OidcUser.super.getPhoneNumberVerified();
		}

		@Override
		public AddressStandardClaim getAddress() {
			return OidcUser.super.getAddress();
		}

		@Override
		public Instant getUpdatedAt() {
			return OidcUser.super.getUpdatedAt();
		}

		@Override
		public List<String> getAudience() {
			return OidcUser.super.getAudience();
		}

		@Override
		public Instant getExpiresAt() {
			return OidcUser.super.getExpiresAt();
		}

		@Override
		public Instant getIssuedAt() {
			return OidcUser.super.getIssuedAt();
		}

		@Override
		public Instant getAuthenticatedAt() {
			return OidcUser.super.getAuthenticatedAt();
		}

		@Override
		public String getNonce() {
			return OidcUser.super.getNonce();
		}

		@Override
		public String getAuthenticationContextClass() {
			return OidcUser.super.getAuthenticationContextClass();
		}

		@Override
		public List<String> getAuthenticationMethods() {
			return OidcUser.super.getAuthenticationMethods();
		}

		@Override
		public String getAuthorizedParty() {
			return OidcUser.super.getAuthorizedParty();
		}

		@Override
		public String getAccessTokenHash() {
			return OidcUser.super.getAccessTokenHash();
		}

		@Override
		public String getAuthorizationCodeHash() {
			return OidcUser.super.getAuthorizationCodeHash();
		}
	}
}

