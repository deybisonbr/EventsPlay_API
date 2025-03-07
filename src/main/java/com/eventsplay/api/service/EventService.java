package com.eventsplay.api.service;

import com.eventsplay.api.domain.event.Event;
import com.eventsplay.api.domain.event.EventRequestDTO;
import com.eventsplay.api.domain.event.EventResponseDTO;
import com.eventsplay.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventService {
    private static final String UPLOAD_DIR = "uploads/";
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png");

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AdressService adressService;

    @Async
    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImage(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());

        long timestamp = convertDateToMillis(data.date());
        newEvent.setDate(new Date(timestamp));

        newEvent.setRemote(data.remote());
        newEvent.setImgUrl(imgUrl);

        this.eventRepository.save(newEvent);

        if(!data.remote()){
            this.adressService.createAdress(data, newEvent);
        }


        return newEvent;

    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String state, Date startDate, Date endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.eventRepository.findFilteredEvents(new Date(), title, city, state, startDate, endDate, pageable);

        List<EventResponseDTO> list = eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getState() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList(); return list;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        Date yesterday = calendar.getTime();

        System.out.println(yesterday);

        Page<Event> eventsPage = this.eventRepository.findUpcomingEvents(yesterday,pageable);

        List<EventResponseDTO> list = eventsPage.map(event -> new EventResponseDTO(
                event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getAddress() != null ? event.getAddress().getCity() : "Remote", //Lembrar de tratar o endereço remote dentro do front
                    event.getAddress() != null ? event.getAddress().getState() : "Remote",
                    event.getRemote(),
                    event.getEventUrl(),
                    event.getImgUrl())
        ).stream().toList();
        return list;
    }

    public String uploadImage(MultipartFile multipartFile) {
        try {
            String extension = getFileExtension(multipartFile.getOriginalFilename()).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new RuntimeException("Invalid image format. Only JPG, JPEG, and PNG are accepted.");
            }

            // Criar diretório se não existir
            Path uploadPath = Path.of(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

//            String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename().replaceAll("\\s+", "_");
            String fileName = UUID.randomUUID().toString() + "." + extension;

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Error saving the image: " + e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return index == -1 ? "" : fileName.substring(index + 1);
    }

    private long convertDateToMillis(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure consistency in time conversion
        try {
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Please use 'yyyy-MM-dd HH:mm:ss'", e);
        }
    }
}


