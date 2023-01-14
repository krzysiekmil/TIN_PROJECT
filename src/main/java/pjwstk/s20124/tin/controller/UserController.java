package pjwstk.s20124.tin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.UserDto;
import pjwstk.s20124.tin.model.mapper.UserMapper;
import pjwstk.s20124.tin.services.CrudApi;
import pjwstk.s20124.tin.services.UserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController implements CrudApi<UserDto> {
    private final UserService service;
    private final UserMapper userMapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto dto) {

        User entity = userMapper.dtoToUser(dto);
        User user = service.create(entity);

        return userMapper.userToDto(user);
    }

    @Override
    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto dto) {

        User entity = userMapper.dtoToUser(dto);
        User user = service.update(entity);

        return userMapper.userToDto(user);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Override
    @GetMapping("/{id}")
    public  Optional<UserDto> getOne(@PathVariable Long id) {
        return service.getOne(id)
            .map(userMapper::userToDto);

    }

    @Override
    @GetMapping
    public Collection<UserDto> getList() {
        return service.getList()
            .stream()
            .map(userMapper::userToDto)
            .toList();
    }
}
