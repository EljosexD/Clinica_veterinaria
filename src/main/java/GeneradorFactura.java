/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author eljot
 */
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
// Vaya esto son para generar el pdf, pd : use la libreria itext7
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

public class GeneradorFactura {
    private String cliente;
    private String mascota;
    private String servicio;
    private String detalleCita;
    private Double precio;

    public GeneradorFactura(String cliente, String fecha, String motivo, Double precio) {
        this.cliente = cliente;
        this.mascota = fecha;
        this.servicio = motivo;
        this.precio = precio;
    }
    private double Iva(){
        return 0.5;
    }
    private double RecargoDeTarjeta(){
        return 5000;
    }
    private double DescuentoEfectivo(){
        return 3500;
    }

    private double TotalEfectivo(){
        return Iva()+ this.precio - DescuentoEfectivo();
    }
    private double TotalDigital(){
        return Iva()+ this.precio + RecargoDeTarjeta();
    }
    private void agregarCabecera(Document document) throws Exception {
        String imagePath = "src/main/resources/img/icons/Orange_Minimalist_Cat_Illustration_Animal_Hospital_Logo-removebg-preview.png";
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData).setWidth(250).setHeight(100);

        Paragraph title = new Paragraph("FACTURA DE SERVICIOS VETERINARIOS")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold();

        Paragraph subtitulo = new Paragraph("Clínica Veterinaria Light Cat")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(14);
        document.add(image);
        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(subtitulo);
        document.add(new Paragraph("\n"));
    }
    private void agregarContenido(Document document, boolean esEfectivo) {
        document.add(new Paragraph("-------------------------------------------- "));
        document.add(new Paragraph("DATOS DEL CLIENTE"));
        document.add(new Paragraph("Nombre del cliente: " + cliente));
        document.add(new Paragraph("Nombre de mascota : " + mascota));
        document.add(new Paragraph("Servicio: " + servicio));
        document.add(new Paragraph("-------------------------------------------- "));
        document.add(new Paragraph("DETALLE DE PAGO"));
        document.add(new Paragraph("Precio base: $" + String.format("%.2f", precio)));
        document.add(new Paragraph("IVA (12%): $" + String.format("%.2f", Iva())));
        if (esEfectivo) {
            document.add(new Paragraph("Descuento por pago en efectivo: -$" + String.format("%.2f", DescuentoEfectivo())));
            document.add(new Paragraph("TOTAL A PAGAR: $" + String.format("%.2f", TotalEfectivo())));
        } else {
            document.add(new Paragraph("Recargo por pago digital: +$" + String.format("%.2f", RecargoDeTarjeta())));
            document.add(new Paragraph("TOTAL A PAGAR: $" + String.format("%.2f", TotalDigital())));
        }
    }
    public void generarFactura(boolean esEfectivo) {
        String tipo = esEfectivo ? "Efectivo" : "Digital";
        String ruta = "facturas\\Factura" + tipo + "-Cliente_" + cliente + "-motivo_"+ servicio + ".pdf";
        File urlPdf = new File("facturas/Factura" + tipo + "-Cliente_" + cliente + "-motivo_"+ servicio + ".pdf");
            try (PdfWriter pdfWriter = new PdfWriter(urlPdf); PdfDocument pdfDocument = new PdfDocument(pdfWriter); Document document = new Document(pdfDocument)) {
                agregarCabecera(document);
                agregarContenido(document, esEfectivo);
                System.out.println("Factura " + tipo + " generada exitosamente: " + urlPdf.getAbsolutePath());
                File archivo = new File(ruta);
                if (archivo.exists()) {
                    try {
                        Desktop.getDesktop().open(archivo);
                    } catch (IOException ex) {
                    System.getLogger(JPfractura.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                } else {
                    System.out.println("El archivo no existe: " + ruta);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
