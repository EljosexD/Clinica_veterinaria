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

public class Facturas {
    // Sus atributos
    private String nombreCliente;
    private String telefonoCliente;
    private String nombreMascota;
    private String fecha;
    private double precio;
    
 
    
    

    // el constructor

    public Facturas(String nombreCliente, String telefonoCliente, String nombreMascota, String fecha, double precio) {
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.nombreMascota = nombreMascota;
        this.fecha = fecha;
        this.precio = precio;
    }


    //los set y get


    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    //metodo sobre el precio
    private double iva(){
        return this.precio * 0.12;
    }
    private double total(){
        return  iva() + this.precio;
    }
    public double mostrariva(){
        return iva();
    }
    public double mostrartotal(){
        return total();
    }
    
    // metodo para mostrar la factura
        public void mostrarFactura(){
        System.out.println("==========================================");
        System.out.println("           CLINICA VETERINARIA           ");
        System.out.println("==========================================");
        System.out.println("Fecha: " + this.fecha);
        System.out.println("------------------------------------------");
        System.out.println("DATOS DEL CLIENTE:");
        System.out.println("Nombre: " + this.nombreCliente);
        System.out.println("Teléfono: " + this.telefonoCliente);
        System.out.println("Nombre de la mascota: " + this.nombreMascota);
        System.out.println("------------------------------------------");
        System.out.println("DETALLE DE PAGO:");
        System.out.println("Precio base: $" + String.format("%.2f", this.precio));
        System.out.println("IVA (12%): $" + String.format("%.2f", iva()));
        System.out.println("Total a pagar: $" + String.format("%.2f", total()));
        System.out.println("==========================================");
    }

    public void generarFacturaPdf() {
        // varaibles para hacer el pdf de la factura
        File urlPdf = new File("facturas\\Factura "+ this.nombreCliente +".pdf");
        try(PdfWriter pdfWriter = new PdfWriter(urlPdf);PdfDocument pdfDocument = new PdfDocument(pdfWriter);Document document = new Document (pdfDocument)){ // truco para no usar el .close
        // Preparando la imagen
        String imagePath = "src\\main\\resources\\img\\icons\\Orange_Minimalist_Cat_Illustration_Animal_Hospital_Logo-removebg-preview.png"; // puede ser .png, .jpg, etc
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData);
        image.setWidth(250); 
        image.setHeight(100); 
        // Preparando el titulo y nombre de marca
        Paragraph title = new Paragraph("FACTURA DE SERVICIOS VETERINARIOS").setTextAlignment(TextAlignment.CENTER).setFontSize(18).setBold();
        Paragraph subtitulo = new Paragraph("Clinica veterinaria Light Cat").setTextAlignment(TextAlignment.CENTER).setFontSize(14);
        document.add(image);
        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(subtitulo);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Fecha : " + this.fecha));
        document.add(new Paragraph("-------------------------------------------- "));
        document.add(new Paragraph("DATOS DEL CLIENTE"));
        document.add(new Paragraph("Nombre: " + this.nombreCliente));
        document.add(new Paragraph("Teléfono: " + this.telefonoCliente));
        document.add(new Paragraph("Nombre de la mascota: " + this.nombreMascota));
        document.add(new Paragraph("-------------------------------------------- ")); 
        document.add(new Paragraph("DETALLE DE PAGO"));
        document.add(new Paragraph("Precio base: $" + String.format("%.2f", this.precio)));
        document.add(new Paragraph("IVA (12%): $" + String.format("%.2f", iva())));
        document.add(new Paragraph("Total a pagar: $" + String.format("%.2f", total())));
        System.out.println("Factura PDF generada exitosamente en: " + urlPdf.getAbsolutePath());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}