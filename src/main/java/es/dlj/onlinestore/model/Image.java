package es.dlj.onlinestore.model;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Image {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    private Blob imageFile;
    
    private String fileName;
    private String contentType;
    private boolean isMainImage;

    public Image() {
    }

    public Image( Blob imageFile, String fileName, String contentType) {
        this.imageFile = imageFile;
        this.fileName = fileName;
        this.contentType = contentType;
        this.isMainImage = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Blob getimageFile() {
        return imageFile;
    }

    public void setimageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType){
        switch (contentType) {
            case "image/jpeg":
                this.contentType = ".jpg";
                break;
                
            case "image/png":
                this.contentType = ".png";
                break;

            case "image/gif":
                this.contentType =  ".gif";
                break;

            default:
                this.contentType = ".jpg";
        }
    }

    public String getContentType (){
        return this.contentType;
    }

    public void setIsMainImage(boolean value){
        this.isMainImage = value;
    }

    public boolean getIsMainImage(){
        return this.isMainImage;
    }
}
