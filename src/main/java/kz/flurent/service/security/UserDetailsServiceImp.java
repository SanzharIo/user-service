package kz.flurent.service.security;

import kz.flurent.model.entity.User;
import kz.flurent.model.response.errors.ErrorCode;
import kz.flurent.model.response.errors.ServiceException;
import kz.flurent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("User not found", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
