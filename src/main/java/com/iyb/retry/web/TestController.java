package com.iyb.retry.web;

import com.alibaba.fastjson.JSONObject;
import com.iyb.retry.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author iyb-wangyanbing
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RequestService requestService;

    @GetMapping("/retry")
    public JSONObject retry() {
        return JSONObject.parseObject(requestService.run(new Object[]{
                "https://www.baidu.com", "{}"
        }, null).toString());
    }
}
