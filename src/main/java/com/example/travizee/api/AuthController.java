package com.example.travizee.api;

import com.example.travizee.dao.LoginRepository;
import com.example.travizee.model.UserModel;
import com.example.travizee.model.facebook.FacebookPicture;
import com.example.travizee.payload.*;
import com.example.travizee.security.JwtTokenProvider;
import com.example.travizee.service.FacebookService;
import com.example.travizee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    LoginRepository loginRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    FacebookService facebookService;

    @Autowired
    private UserService userService;

    @PostMapping("/facebook/signin")
    public  ResponseEntity<?> facebookAuth(@RequestBody FacebookLoginRequest facebookLoginRequest) {
    //    log.info("facebook login {}", facebookLoginRequest);
        SignupResponse signupResponse = facebookService.loginUser(facebookLoginRequest.getAccessToken());
        //return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        if (signupResponse != null){
        UserModel  userModel = userService.findById(Long.valueOf(signupResponse.getId())).orElseThrow();
            return new ResponseEntity(new FbLoginResponse(true, userModel.getUsername(), userModel.getEmail(), signupResponse.getFacebookPicture().getData().getUrl()), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(new ApiResponse(false, "Please check your email/password or try again later."), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

        if(loginRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(loginRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        UserModel user = new UserModel(signUpRequest.getEmail().substring(0,signUpRequest.getEmail().indexOf('@')),signUpRequest.getEmail(), signUpRequest.getPassword());
        String token = tokenProvider.generateToken(signUpRequest.getEmail());
        user.setToken(token);
        user.setId(Long.valueOf(UUID.randomUUID().toString()));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserModel result = loginRepository.save(user);


//        ConfirmationToken confirmationToken = new ConfirmationToken(user, token, user.getId());
//
//        confirmationTokenRepository.save(confirmationToken);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getEmail()).toUri();

        return created(location).body(new SignupResponse(true, "User registered Successfully!", token,user.getId().toString(), new FacebookPicture()));
    }


    @GetMapping("/login_error")
    public ResponseEntity<?> handleFailedLogin(@RequestParam("email")String email){
        if (!loginRepository.existsByEmail(email)){
            return new ResponseEntity(new ApiResponse(false, "User not found, Please register."), HttpStatus.UNAUTHORIZED);

        }

        return new ResponseEntity(new ApiResponse(false, "Please check your email/password or try again later."), HttpStatus.BAD_REQUEST);

    }
}
