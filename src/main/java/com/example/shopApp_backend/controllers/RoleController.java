package com.example.shopApp_backend.controllers;


import com.example.shopApp_backend.model.Role;
import com.example.shopApp_backend.services.RoleService.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor

public class RoleController {
    private final IRoleService roleService;
    @GetMapping("")
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
