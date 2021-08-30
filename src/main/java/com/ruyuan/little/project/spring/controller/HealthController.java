package com.ruyuan.little.project.spring.controller;

import com.ruyuan.little.project.common.dto.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author zhuyc
 * @date 2021/08/30 07:21
 **/
@RestController
@RequestMapping("/")
public class HealthController {

    public HealthController() {
        System.out.println("init");
    }

    @GetMapping(value = "/")
    public CommonResponse health() {
        return CommonResponse.success();
    }

}
