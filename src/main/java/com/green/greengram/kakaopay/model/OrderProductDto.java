package com.green.greengram.kakaopay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class OrderProductDto {
    private Long productId;
    private int quantity;
}