package com.effective_mobile.test_project.controllers;

import com.effective_mobile.test_project.model.User;
import com.effective_mobile.test_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.effective_mobile.test_project.controllers.UserController.getListResponseEntity;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
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
}