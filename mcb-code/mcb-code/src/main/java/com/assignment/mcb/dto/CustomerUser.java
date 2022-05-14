package com.assignment.mcb.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomerUser extends User {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long customerId;

    public CustomerUser(long customerId,String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        setCustomerId(customerId);
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
