package es.dlj.onlinestore.model;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

@Entity
public class ProductImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    private Blob imageFile;
    
    private String fileName;
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage() {
    }

    public ProductImage( Blob imageFile, String fileName, Product product, String contentType) {
        this.imageFile = imageFile;
        this.fileName = fileName;
        this.product = product;
        this.contentType = contentType;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setContentType(String contentType){
        switch (contentType) {
            case "image/jpeg":
                this.contentType = ".jpg";
            case "image/png":
                this.contentType = ".png";
            case "image/gif":
                this.contentType =  ".gif";
            default:
                this.contentType = ".jpg";
        }
    }

    public String getContentType (){
        return this.contentType;
    }
}
