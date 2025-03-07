package com.eventsplay.api.controller;

import com.eventsplay.api.domain.coupon.Coupon;
import com.eventsplay.api.domain.coupon.CouponRequestDTO;
import com.eventsplay.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable() UUID eventId, @RequestBody CouponRequestDTO couponRequestDTO) {
        Coupon coupon = couponService.addCouponToEvent(eventId, couponRequestDTO);
        return ResponseEntity.ok(coupon);
    }
}
