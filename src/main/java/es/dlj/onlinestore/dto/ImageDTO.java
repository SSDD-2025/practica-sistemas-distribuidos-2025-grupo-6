package es.dlj.onlinestore.dto;

import java.sql.Blob;

public record ImageDTO(
    long id,
    Blob imageFile,
    String contentType
){}