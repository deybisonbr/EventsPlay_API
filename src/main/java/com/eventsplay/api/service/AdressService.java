package com.eventsplay.api.service;

import com.eventsplay.api.domain.adress.Address;
import com.eventsplay.api.domain.event.Event;
import com.eventsplay.api.domain.event.EventRequestDTO;
import com.eventsplay.api.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAdress(EventRequestDTO eventRequestDTO, Event event) {
        System.out.println(eventRequestDTO);
        Address address = new Address();
        address.setCity(eventRequestDTO.city());
        address.setState(eventRequestDTO.state());
        address.setEvent(event);

        return addressRepository.save(address);

    }
}
