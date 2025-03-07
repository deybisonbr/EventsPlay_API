package com.eventsplay.api.controller;

import com.eventsplay.api.domain.event.Event;
import com.eventsplay.api.domain.event.EventMinimalResponseDTO;
import com.eventsplay.api.domain.event.EventRequestDTO;
import com.eventsplay.api.domain.event.EventResponseDTO;
import com.eventsplay.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

//    @PostMapping("/create")
//    public ResponseEntity<Event> create(@RequestBody EventRequestDTO body){
//        Event newEvent = eventService.createEvent(body);
//        return ResponseEntity.ok(newEvent);
//    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<EventMinimalResponseDTO> create(@RequestParam("title") String title,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam("date") String date,
                                        @RequestParam("city") String city,
                                        @RequestParam("state") String state,
                                        @RequestParam("remote") Boolean remote,
                                        @RequestParam("eventUrl") String eventUrl,
                                        @RequestParam(value = "image", required = false) MultipartFile image

    ) {
        EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, state, remote, eventUrl, image);
        Event newEvent = eventService.createEvent(eventRequestDTO);

        EventMinimalResponseDTO eventMinimalResponseDTO = new EventMinimalResponseDTO(newEvent.getId(), newEvent.getTitle(), newEvent.getDescription());

        return ResponseEntity.ok(eventMinimalResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int size) {
        List<EventResponseDTO> allEvents = this.eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "1") int size,
                                                               @RequestParam(required = false) String title,
                                                               @RequestParam(required = false) String city,
                                                               @RequestParam(required = false) String state,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate
) {
        List<EventResponseDTO> allEvents = this.eventService.getFilteredEvents(page, size, title, city, state, startDate, endDate);
        return ResponseEntity.ok(allEvents);
    }

    // Endpoint para upload de imagem
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String fileUrl = eventService.uploadImage(image);
            return ResponseEntity.ok("File saved successfully in path: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error send file: " + e.getMessage());
        }
    }
}
