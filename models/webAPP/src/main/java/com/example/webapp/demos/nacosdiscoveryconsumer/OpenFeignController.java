/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.webapp.demos.nacosdiscoveryconsumer;

import com.example.webapp.demos.nacosconfig.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "OpenFeignController", description = "测试相关接口")
public class OpenFeignController {


    @Autowired
    private User user;

    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @GetMapping("/feign/echo/{message}")
    public String feignEcho(@PathVariable String message) {
        return message + user.toString();
    }


    @Operation(summary = "管理员添加用户",
            description = "根据姓名添加用户并返回",
            parameters = {
                    @Parameter(name = "name", description = "姓名")
            },
            responses = {
                    @ApiResponse(description = "返回添加的用户", content = @Content(mediaType = "application/json", schema = @Schema(anyOf = {String.class, User.class}))),
                    @ApiResponse(responseCode = "400", description = "返回400时候错误的原因")
            }
    )
    String addUser(String name){
        return "addUser:"+name;
    }

    @Operation(summary = "管理员删除用户", description = "根据姓名删除用户")
    @ApiResponse(description = "返回添加的用户", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    User delUser(@Parameter(description = "姓名") String name){
        return new User();
    }


    @Operation(summary = "管理员更新用户", description = "管理员根据姓名更新用户")
    @ApiResponse(description = "返回更新的用户", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    String updateUser(@Parameter(schema = @Schema(implementation = User.class), required = true, description = "用户类") User user){
        return "addUser:"+user;
    }
}
