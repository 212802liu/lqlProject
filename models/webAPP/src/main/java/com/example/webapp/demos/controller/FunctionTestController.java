package com.example.webapp.demos.controller;

import com.example.webapp.demos.entity.User;
import com.example.webapp.demos.service.FunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fun")
@Slf4j
@Tag(name = "FunctionTestController", description = "测试-功能相关接口")
public class FunctionTestController {

    @Autowired
    private FunctionService functionService;

    @Operation(summary = "发html 邮件功能", description = "使用GenerateHtmlContextUtil 工具类")
//    @ApiResponse(description = "返回添加的用户", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @GetMapping("/htmlContentMail")
    String htmlContentMail(){
        functionService.htmlContentMail();
        return "OK";
    }
}
