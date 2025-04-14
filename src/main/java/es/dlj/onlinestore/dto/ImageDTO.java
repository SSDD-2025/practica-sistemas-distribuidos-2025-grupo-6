package es.dlj.onlinestore.dto;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record ImageDTO(
    long id,
    @JsonIgnore
    Blob imageFile,
    String contentType
){}