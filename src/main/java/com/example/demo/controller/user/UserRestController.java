package com.example.demo.controller.user;

import com.example.demo.model.jwt.JWTSerializer;
import com.example.demo.model.jwt.UserJWTPayload;
import com.example.demo.model.user.Email;
import com.example.demo.model.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.controller.user.UserModel.fromUserAndToken;
import static org.springframework.http.ResponseEntity.of;

@RestController
@RequestMapping("/api")
class UserRestController {

    private final UserService userService;
    private final JWTSerializer jwtSerializer;

    UserRestController(UserService userService, JWTSerializer jwtSerializer) {
        this.userService = userService;
        this.jwtSerializer = jwtSerializer;
    }

    @PostMapping("/users")
    public UserModel postUser(@Valid @RequestBody UserPostRequestDTO dto) {
        final var userSaved = userService.signUp(dto.toSignUpRequest());
        return fromUserAndToken(userSaved, jwtSerializer.jwtFromUser(userSaved));
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserModel> loginUser(@Valid @RequestBody UserLoginRequestDTO dto) {
        return of(userService.login(new Email(dto.getEmail()), dto.getPassword())
                .map(user -> fromUserAndToken(user, jwtSerializer.jwtFromUser(user))));
    }

    @GetMapping("/user")
    public ResponseEntity<UserModel> getUser(@AuthenticationPrincipal UserJWTPayload jwtPayload) {
        return of(userService.findById(jwtPayload.getUserId())
                .map(user -> fromUserAndToken(user, getCurrentCredential())));
    }

    @PutMapping("/user")
    public UserModel putUser(@AuthenticationPrincipal UserJWTPayload jwtPayload,
                             @Valid @RequestBody UserPutRequestDTO dto) {
        final var userUpdated = userService.updateUser(jwtPayload.getUserId(), dto.toUpdateRequest());
        return fromUserAndToken(userUpdated, getCurrentCredential());
    }

    private static String getCurrentCredential() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials()
                .toString();
    }
}
