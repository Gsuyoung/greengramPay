package com.green.greengram.kakaopay.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KakaoPayReadyReq {
    private List<OrderProductDto> productList;

    /*
    [
        { productId: 100, quantity: 10 },
        { productId: 200, quantity: 20 },
        { productId: 300, quantity: 30 },
    ]

    {
        100 : { productId: 100, quantity: 10 },
        200 : { productId: 200, quantity: 20 },
        300 : { productId: 300, quantity: 30 }
    }


     */
}