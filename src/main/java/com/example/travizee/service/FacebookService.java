package com.example.travizee.service;

import com.example.travizee.exception.AppException;
import com.example.travizee.model.UserModel;
import com.example.travizee.model.facebook.FacebookUser;
import com.example.travizee.payload.SignupResponse;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.travizee.client.FacebookClient;
import com.example.travizee.security.JwtTokenProvider;

import java.io.Console;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

@Service
@Slf4j
public class FacebookService {
    @Autowired private FacebookClient facebookClient;
    @Autowired private UserService userService;
    @Autowired private JwtTokenProvider tokenProvider;

    public SignupResponse loginUser(String fbAccessToken) {
        FacebookUser facebookUser = facebookClient.getUser(fbAccessToken);
        return (SignupResponse) userService.findById(Long.valueOf(facebookUser.getId()))
                .or(() -> {
                    UserModel userModel = convertTo(facebookUser);
                    return Optional.ofNullable(userService.save(userModel));
                })
                .map(new Function<UserModel, UsernamePasswordAuthenticationToken>() {
                    @Override
                    public UsernamePasswordAuthenticationToken apply(UserModel userDetails) {
                        return new UsernamePasswordAuthenticationToken(
                                userDetails, null);
                    }
                })
                .map(new Function<UsernamePasswordAuthenticationToken, String>() {
                    @Override
                    public String apply(UsernamePasswordAuthenticationToken authentication) {
                        return tokenProvider.generateToken(authentication);
                    }
                })
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) {
                        return new SignupResponse(true,"User retrieved succesfully", s , facebookUser.getId(), facebookUser.getPicture());
                    }
                })
                .orElseThrow(() ->
                        new AppException("unable to login facebook user id " + facebookUser.getId()));
    }

    private UserModel convertTo(FacebookUser facebookUser) {
        UserModel userModel = new UserModel(Long.valueOf(facebookUser.getId()),facebookUser.getFirstName(), facebookUser.getEmail(), generatePassword(8));
        userModel.setId(Long.valueOf(facebookUser.getId()));
        return userModel;
    }

    private String generateUsername(String firstName, String lastName) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%s.%s.%06d", firstName, lastName, number);
    }

    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }



}
