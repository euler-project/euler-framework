/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013-2017 cFrost.sun(孙宾, SUN BIN) 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * For more information, please visit the following website
 * 
 * https://eulerproject.io
 * https://github.com/euler-form/web-form
 * https://cfrost.net
 */
package net.eulerframework.web.module.authentication.entity;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.CredentialsContainer;

import net.eulerframework.web.module.authentication.principal.EulerUserDetails;

/**
 * @author cFrost
 *
 */
public interface EulerUserEntity extends CredentialsContainer {

    /**
     * Returns the id of the user.
     * 
     * @return the id of the user
     */
    String getUserId();

    /**
     * Returns if the user is a root user
     * 
     * @return <code>true</code> the user is a root user, <code>false</code> or
     *         <code>null</code> the user isn't a root user
     */
    Boolean isRoot();

    /**
     * Returns the authorities granted to the user.
     *
     * @return the authorities
     */
    Collection<? extends EulerAuthorityEntity> getAuthorities();

    /**
     * @param encode
     */
    void setPassword(String password);

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    String getPassword();

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code> .
     *
     * @return the username
     */
    String getUsername();

    /**
     * Indicates whether the user's account has expired. An expired account
     * cannot be authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie
     *         non-expired), <code>false</code> or <code>null</code> if no
     *         longer valid (ie expired)
     */
    Boolean isAccountNonExpired();

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code>
     *         or <code>null</code> otherwise
     */
    Boolean isAccountNonLocked();

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie
     *         non-expired), <code>false</code> or <code>null</code> if no
     *         longer valid (ie expired)
     */
    Boolean isCredentialsNonExpired();

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot
     * be authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code>
     *         otherwise
     */
    Boolean isEnabled();

    default EulerUserDetails toEulerUserDetails() {
        return new EulerUserDetails(this);
    }

    /**
     * @param username
     */
    void setUsername(String username);

    /**
     * @param email
     */
    void setEmail(String email);

    /**
     * @param mobile
     */
    void setMobile(String mobile);

    /**
     * @return
     */
    String getEmail();

    /**
     * @return
     */
    String getMobile();

    /**
     * @param enabled
     */
    void setEnabled(Boolean enabled);

    /**
     * @param accountNonExpired
     */
    void setAccountNonExpired(Boolean accountNonExpired);

    /**
     * @param accountNonLocked
     */
    void setAccountNonLocked(Boolean accountNonLocked);

    /**
     * @param credentialsNonExpired
     */
    void setCredentialsNonExpired(Boolean credentialsNonExpired);

    /**
     * @param root
     */
    void setRoot(Boolean root);
    
    void setRegistTime(Date registDate);
    
    Date getRegistTime();
}