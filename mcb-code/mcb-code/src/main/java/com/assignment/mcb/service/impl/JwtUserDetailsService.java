package com.assignment.mcb.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.assignment.mcb.dto.CustomerUser;
import com.assignment.mcb.model.Customer;
import com.assignment.mcb.repository.CustomerRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	private final CustomerRepository customerRepository;

	@Autowired
	public JwtUserDetailsService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
				MessageFormat.format("User not found with username {0}", username)));
		return new CustomerUser(customer.getCustomerId(), customer.getUserName(), customer.getPassword(),
				new ArrayList<>());
	}
}