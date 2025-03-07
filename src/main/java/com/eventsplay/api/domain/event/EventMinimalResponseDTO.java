package com.eventsplay.api.domain.event;

import java.util.UUID;

public record EventMinimalResponseDTO(UUID id, String title, String description) {
}
