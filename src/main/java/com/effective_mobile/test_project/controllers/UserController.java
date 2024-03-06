package com.effective_mobile.test_project.controllers;

import com.effective_mobile.test_project.model.User;
import com.effective_mobile.test_project.request.CreateUserRequest;
import com.effective_mobile.test_project.request.DeleteContactsRequest;
import com.effective_mobile.test_project.request.UpdateContactsRequest;
import com.effective_mobile.test_project.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User newUser = userService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getDateOfBirth(),
                request.getEmail(),
                request.getPhone(),
                request.getInitialBalance()
        );

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/update-contacts")
    public ResponseEntity<User> updateContacts(@RequestBody UpdateContactsRequest request) {
        User updatedUser = userService.updateUserContacts(
                request.getUsername(),
                request.getNewEmail(),
                request.getNewPhone()
        );

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/delete-contacts")
    public ResponseEntity<User> deleteContacts(@RequestBody DeleteContactsRequest request) {
        User deletedUser = userService.deleteUserContacts(
                request.getUsername(),
                request.getContactType()
        );

        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) LocalDate birthDate,
                                                  @RequestParam(required = false) String phone,
                                                  @RequestParam(required = false) String fullName,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder) {
        return getListResponseEntity(birthDate, phone, fullName, email, page, size, sortBy, sortOrder, userService);
    }

    static ResponseEntity<List<User>> getListResponseEntity(@RequestParam(required = false) LocalDate birthDate, @RequestParam(required = false) String phone, @RequestParam(required = false) String fullName, @RequestParam(required = false) String email, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortOrder, UserService userService) {
        Sort.Direction sortDirection = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection,  sortBy));

        List<User> users = userService.searchUsers(birthDate, phone, fullName, email, pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
