package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.ProfileDto;
import com.upiicsa.ApiSIP.Dto.UserNameDto;
import com.upiicsa.ApiSIP.Service.UserService;
import com.upiicsa.ApiSIP.Utils.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/my-name")
    @PreAuthorize("hasAnyRole('ALUMNO', 'OPERATIVO', 'ADMINISTRADOR')")
    public ResponseEntity<UserNameDto> getMyName(){
        Integer userId = AuthHelper.getAuthenticatedUserId();

        return ResponseEntity.ok(userService.getName(userId));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ALUMNO', 'OPERATIVO', 'ADMINISTRADOR')")
    public ResponseEntity<ProfileDto> getProfile(){
        Integer userId = AuthHelper.getAuthenticatedUserId();

        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
