package com.iyb.retry.service;

import com.alibaba.fastjson.JSONObject;
import com.iyb.retry.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author iyb-wangyanbing
 */
//@FeignClient(name = "iyb-halley", url = "${iyb.halley.host}")
@FeignClient(value = "http://16136-iyb-halley.test.za-tech.net", configuration = FeignConfiguration.class)
public interface HalleyFeign {
    /**
     * @param mailParam{
     *     "fromMail": "passport@mailserver.iyunbao.com",
     *     "title": "邮件标题",
     *     "userName": "iyunbao",
     *     "toMails": "123@qq.com,456@qq.com",
     *     "bodyHtml": "邮件正文",
     * }
     * @return {"code":"0"}
     */
    @RequestMapping(value = "/int/v1/email/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JSONObject sendMail(@RequestBody JSONObject mailParam);

    @RequestMapping(value = "/int/v2/sms/send", method = RequestMethod.POST)
    JSONObject sendSms(@RequestBody JSONObject mailParam);
}
