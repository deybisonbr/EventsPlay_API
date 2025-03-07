package com.eventsplay.api.repositories;

import com.eventsplay.api.domain.adress.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
