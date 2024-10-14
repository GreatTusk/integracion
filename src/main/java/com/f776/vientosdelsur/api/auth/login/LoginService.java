package com.f776.vientosdelsur.api.auth.login;

import com.f776.vientosdelsur.api.response.NoContentException;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.user.UserRepository;
import com.f776.vientosdelsur.cache.CacheStore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LoginService implements ILoginService {

    public static final int MAX_LOGIN_ATTEMPTS = 5;
    private final CacheStore<String, Integer> userCache;
    private final UserRepository userRepository;


    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        User userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoContentException("No se ha podido encontrar la cuenta. Por favor inicie sesión con una cuenta válida."));

        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userDetails.getEmail()) == null) {
                    userDetails.setLoginAttempts(0);
                    userDetails.setAccountNonLocked(true);
                }

                userDetails.setLoginAttempts(userDetails.getLoginAttempts() + 1);
                userCache.put(userDetails.getEmail(), userDetails.getLoginAttempts());

                if (userCache.get(userDetails.getEmail()) > MAX_LOGIN_ATTEMPTS) {
                    userDetails.setAccountNonLocked(false);
                }

            }
            case LOGIN_SUCCESS -> {
                userDetails.setAccountNonLocked(true);
                userDetails.setLoginAttempts(0);
                userDetails.setLastLogin(LocalDateTime.now());
                userCache.evict(userDetails.getEmail());
            }
        }

        userRepository.save(userDetails);
    }
}
