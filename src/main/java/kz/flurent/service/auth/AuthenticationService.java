package kz.flurent.service.auth;

import kz.flurent.model.entity.Role;
import kz.flurent.model.entity.User;
import kz.flurent.model.request.AuthenticationRequest;
import kz.flurent.model.response.UserResponse;
import kz.flurent.model.response.errors.ErrorCode;
import kz.flurent.model.response.errors.ServiceException;
import kz.flurent.mpper.UserMapper;
import kz.flurent.repository.RoleRepository;
import kz.flurent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(AuthenticationRequest authPhoneRequest) {
        String username = authPhoneRequest.getUsername();
        boolean isUserExist = userRepository.existsByUsername(username);

        if (isUserExist) {
            throw new ServiceException("This user is already exist", ErrorCode.ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        Role role = roleRepository.findByRoleName("SELLER");

        User user = User.builder()
                .username(authPhoneRequest.getUsername())
                .firstName(authPhoneRequest.getFirstName())
                .lastName(authPhoneRequest.getLastName())
                .patronymic(authPhoneRequest.getPatronymic())
                .active(true)
                .block(false)
                .role(role)
                .build();
        userRepository.save(user);
        return userMapper.toResponse(user);
    }
}
