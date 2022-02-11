package com.ing.brokage.service.impl;

import com.ing.brokage.configuration.jwt.JwtTokenUtil;
import com.ing.brokage.modal.request.TokenRequest;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements UserDetailsService {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationServiceImpl(CustomerService customerService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer loggedInUser = customerService.findByUsername(username);

        if (loggedInUser != null) {
            return loggedInUser;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public String authenticate(TokenRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        Customer user = customerService.findByUsername(authenticationRequest.getUsername());
        return jwtTokenUtil.generateToken(user);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}