package com.green.greengram.kakaopay;

import com.green.greengram.config.feignclient.FeignClientKakaoPayConfiguration;
import com.green.greengram.kakaopay.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "kakaoPayApi"
           , url = "https://open-api.kakaopay.com/online/v1/payment"
           , configuration = { FeignClientKakaoPayConfiguration.class })
public interface KakaoPayFeignClient {


    @PostMapping(value = "/ready")
    KakaoPayReadyRes postReady(KakaoPayReadyFeignReq req);

    @PostMapping(value = "/approve")
    KakaoPayApproveRes postApprove(KakaoPayApproveFeignReq req);
}
