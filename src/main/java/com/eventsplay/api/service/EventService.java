package com.eventsplay.api.service;

import com.eventsplay.api.domain.event.Event;
import com.eventsplay.api.domain.event.EventRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;

@Service
public class EventService {
    private static final String UPLOAD_DIR = "uploads/";

    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        if(data.image() != null){
            imgUrl = this.uploadImage(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);

        return newEvent;

    }

    private String uploadImage(MultipartFile multipartFile){
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileDir = UPLOAD_DIR + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(fileDir));
            return "Imagem salva com sucesso em: " + fileDir;
        } catch (IOException e) {
            return "Erro ao salvar a imagem: " + e.getMessage();
        }

    }
}
