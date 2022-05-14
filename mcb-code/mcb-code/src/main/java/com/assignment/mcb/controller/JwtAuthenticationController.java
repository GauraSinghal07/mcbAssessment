package com.assignment.mcb.controller;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.mcb.config.JwtTokenUtil;
import com.assignment.mcb.dto.JwtRequest;
import com.assignment.mcb.dto.JwtResponse;
import com.assignment.mcb.model.Customer;
import com.assignment.mcb.repository.CustomerRepository;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@PostConstruct
	public void addUsers() {
		long id = 1;
		createUser(id++, "gaura_1", "gaura123", "gaura", "a");
		createUser(id++, "gaura_2", "gaura123", "gaura", "a");
		createUser(id++, "gaura_3", "gaura123", "gaura", "a");
	}

	private void createUser(long id, String userName, String password, String firstName, String lastName) {
		Customer customer = new Customer();
		customer.setCustomerId(id);
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setPassword(passwordEncoder.encode(password));
		customer.setUserName(userName);
		customerRepository.save(customer);
	}
}
