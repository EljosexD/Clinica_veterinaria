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
public class FacturaEfectivo extends Facturas {
    private double descuento = 0.05; // 5% de descuento por pago en efectivo
    public FacturaEfectivo(String nombreCliente, String telefonoCliente, String nombreMascota, String fecha, double precio) {
        super(nombreCliente, telefonoCliente, nombreMascota, fecha, precio);
    }
    private double descuento(){
        return (mostrariva() + this.precio) * descuento;
    }
    @Override
    protected double total(){
        return mostrariva() + this.precio - descuento();
    }
    @Override
    protected void generarFacturaPdf() {
        // varaibles para hacer el pdf de la factura
        File urlPdf = new File("facturas\\FacturaEfectivo "+ nombreCliente +".pdf");
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
        document.add(new Paragraph("Fecha : " + fecha));
        document.add(new Paragraph("-------------------------------------------- "));
        document.add(new Paragraph("DATOS DEL CLIENTE"));
        document.add(new Paragraph("Nombre: " + nombreCliente));
        document.add(new Paragraph("Teléfono: " + telefonoCliente));
        document.add(new Paragraph("Nombre de la mascota: " + nombreMascota));
        document.add(new Paragraph("-------------------------------------------- ")); 
        document.add(new Paragraph("DETALLE DE PAGO"));
        document.add(new Paragraph("Precio base: $" + String.format("%.2f", this.precio)));
        document.add(new Paragraph("IVA (12%): $" + String.format("%.2f", mostrariva())));
        document.add(new Paragraph("Descuento por pago en efectivo (5%): -$" + String.format("%.2f", (descuento()))));
        document.add(new Paragraph("Total a pagar: $" + String.format("%.2f", total())));
        System.out.println("Factura PDF generada exitosamente en: " + urlPdf.getAbsolutePath());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    
}
