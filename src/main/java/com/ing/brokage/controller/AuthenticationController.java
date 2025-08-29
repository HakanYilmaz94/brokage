package com.ing.brokage.controller;

import com.ing.brokage.modal.request.TokenRequest;
import com.ing.brokage.modal.response.TokenResponse;
import com.ing.brokage.service.impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ing.brokage.constant.UrlConstants.AUTH;
import static com.ing.brokage.constant.UrlConstants.TOKEN;

@RestController
@RequestMapping(path = AUTH)
@CrossOrigin
public class AuthenticationController {


    private final AuthenticationServiceImpl authenticationServiceImpl;

    @Autowired
    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    @PostMapping(TOKEN)
    public ResponseEntity<TokenResponse> generateToken(@RequestBody TokenRequest tokenRequest) throws Exception {
        String token = authenticationServiceImpl.authenticate(tokenRequest);
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
