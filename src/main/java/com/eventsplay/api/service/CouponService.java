package com.eventsplay.api.service;

import com.eventsplay.api.domain.coupon.Coupon;
import com.eventsplay.api.domain.coupon.CouponRequestDTO;
import com.eventsplay.api.domain.event.Event;
import com.eventsplay.api.repositories.CouponRepository;
import com.eventsplay.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private EventRepository eventRepository;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponRequestDTO) {

        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon newCoupon = new Coupon();
        newCoupon.setEvent(event);
        newCoupon.setCode(couponRequestDTO.code());
        newCoupon.setValid(new Date(couponRequestDTO.valid()));
        newCoupon.setDiscount(couponRequestDTO.discount());

        this.couponRepository.save(newCoupon);
        return newCoupon;
    }
}
