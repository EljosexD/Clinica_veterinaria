import java.io.File;
// Vaya esto son para generar el pdf, pd : use la libreria itext7
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

public abstract class Facturas {
    // Sus atributos
    protected String nombreCliente;
    protected String telefonoCliente;
    protected String nombreMascota;
    protected String fecha;
    protected double precio;
    // el constructor
    public Facturas(String nombreCliente, String telefonoCliente, String nombreMascota, String fecha, double precio) {
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.nombreMascota = nombreMascota;
        this.fecha = fecha;
        this.precio = precio;
    }
    //los set y get
    protected String getNombreCliente() {
        return nombreCliente;
    }

    protected void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    protected String getTelefonoCliente() {
        return telefonoCliente;
    }

    protected void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    protected String getNombreMascota() {
        return nombreMascota;
    }

    protected void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    protected String getFecha() {
        return fecha;
    }

    protected void setFecha(String fecha) {
        this.fecha = fecha;
    }

    protected double getPrecio() {
        return precio;
    }

    protected void setPrecio(double precio) {
        this.precio = precio;
    }

    //metodo sobre el precio
    private double iva(){
        return this.precio * 0.12;
    }

    protected double mostrariva(){
        return iva();
    }

    
    protected abstract double total();
    protected abstract void generarFacturaPdf();
        
    
}