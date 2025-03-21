package es.dlj.onlinestore.domain;

import java.sql.Blob;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Image {

    private static final Map<String, String> EXTENSIONS = Map.of("image/jpeg", ".jpg", "image/png", ".png", "image/gif", ".gif");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    private Blob imageFile;

    private String contentType;

    public Image() {}

    public Image(Blob imageFile, String contentType) {
        this.imageFile = imageFile;
        this.contentType = contentType;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Blob getimageFile() { return imageFile; }
    public void setImageFile(Blob imageFile) { this.imageFile = imageFile; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType){ this.contentType = EXTENSIONS.getOrDefault(contentType, ".jpg"); }
}
