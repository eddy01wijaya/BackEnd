package com.eddywijaya.recruitmentbcaf.controller;

import com.eddywijaya.recruitmentbcaf.dto.validasi.ValLoginDTO;
import com.eddywijaya.recruitmentbcaf.service.AppUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AppUserDetailService appUserDetailService;

    @PostMapping("/login")
    public ResponseEntity<Object> doLogin(@Valid @RequestBody
                                              ValLoginDTO valLoginDTO
            , HttpServletRequest request){
        System.out.println();
        return appUserDetailService.doLogin(appUserDetailService.convertToEntiy(valLoginDTO), request);
    }

}