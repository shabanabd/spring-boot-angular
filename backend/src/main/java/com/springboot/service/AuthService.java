package com.springboot.service;

import com.springboot.dtos.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
