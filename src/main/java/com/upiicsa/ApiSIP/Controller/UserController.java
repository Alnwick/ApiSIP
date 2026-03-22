package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.User.DataDto;
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

    @GetMapping("/data")
    @PreAuthorize("hasAnyRole('OPERATIVO', 'ADMINISTRADOR')")
    public ResponseEntity<DataDto> data(){
        Integer userId = AuthHelper.getAuthenticatedUserId();

        return ResponseEntity.ok(userService.getData(userId));
    }
}
