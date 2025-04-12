package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.dto.UserDto;
import nl.novi.sd.carrental.model.User;
import nl.novi.sd.carrental.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper mapper = new ModelMapper();

    @ResponseBody
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService
                .getAllUsers()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return this.mapToDto(userService.getUser(id));
    }

    @ResponseBody
    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return this.mapToDto(userService.createUser(this.mapToEntity(userDto)));
    }

    @ResponseBody
    @PutMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto updatedUser) {
        return this.mapToDto(userService.updateUser(this.mapToEntity(updatedUser)));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private UserDto mapToDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }
}
