package co.edu.unipiloto.mascotas;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.List;
import android.widget.Toast;
import android.net.Uri;
import java.io.OutputStream;
public class ReporteExporter {

    public static void exportarPDF(Context context, List<SesionEjercicio> sesiones, String nombreMascota, String tipoReporte, Uri uri) {
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Reporte de Ejercicio: " + nombreMascota));
            document.add(new Paragraph("Tipo de reporte: " + tipoReporte));
            document.add(new Paragraph("\n\n"));

            for (SesionEjercicio sesion : sesiones) {
                document.add(new Paragraph("Fecha: " + sesion.getTimestamp()));
                document.add(new Paragraph("Distancia: " + sesion.getDistancia() + " km"));
                document.add(new Paragraph("Duración: " + sesion.getDuracion() + " minutos"));
                document.add(new Paragraph("Calorías quemadas: " + sesion.getCalorias() + " kcal"));
                document.add(new Paragraph("\n"));
            }

            document.close();
            if (outputStream != null) {
                outputStream.close();
            }

            Toast.makeText(context, "PDF exportado correctamente", Toast.LENGTH_SHORT).show();
        } catch (DocumentException | IOException e) {
            Log.e("PDF_EXPORT", "Error al exportar el PDF: " + e.getMessage(), e);
            Toast.makeText(context, "Error al exportar PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
