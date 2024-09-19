package com.example.auth.service;

import com.example.auth.entity.LoginBody;
import com.example.common.core.web.AjaxResult;

public interface LoginService {
    AjaxResult login(LoginBody loginBody);
}
