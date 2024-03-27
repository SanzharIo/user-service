package kz.flurent.service.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import kz.flurent.config.AuditorUtils;
import kz.flurent.model.entity.User;
import kz.flurent.model.response.UserResponse;
import kz.flurent.model.response.errors.ErrorCode;
import kz.flurent.model.response.errors.ServiceException;
import kz.flurent.mpper.UserMapper;
import kz.flurent.predicate.PredicatesBuilder;
import kz.flurent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    @Override
    public Page<UserResponse> findAll(Pageable pageable, Optional<String> search) {
        PredicatesBuilder builder = new PredicatesBuilder();
        if (search.isPresent()) {
            Pattern pattern = Pattern.compile("(\\w+?[.]\\w+?|\\w+?)" +
                    "(:|<|>)" +
                    "(\\w+?\\s\\w+?|\\w+?|\\w+?\\s\\w+?\\s\\w+?|\\w+?[-]\\w+?[-]\\w+?\\w+?|[а-яА-ЯёЁ]+),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search.get() + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build(User.class);
        Page<User> users = userRepository.findAll(exp, pageable);
        return new PageImpl<>(userMapper.toResponse(users.getContent()), pageable, users.getTotalElements());
    }

    @Override
    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("User doesn't exist", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> findByIds(Set<UUID> id) {
        return userMapper.toResponse(userRepository.findAllById(id));
    }

    @Override
    public void blockUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("User doesn't exist", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));
        user.setBlock(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("User doesn't exist", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));
        user.setBlock(false);
        userRepository.save(user);
    }

    @Override
    public UserResponse getUserDetails() {
        User user = userRepository.findByUsername(AuditorUtils.getCurrentUsername()).get();
        return userMapper.toResponse(user);
    }
}
