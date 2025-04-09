package CONTROLLER;/*
package CONTROLLER;

    import java.io.OutputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import org.json.JSONObject;

public class mailSendiblue {

        public static void main(String[] args) {
            try {
                // URL de la API
                URL url = new URL("https://api.brevo.com/v3/smtp/email");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("accept", "application/json");
                con.setRequestProperty("api-key", "TU_API_KEY_DE_SENDINBLUE");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                // Contenido del correo en formato JSON
                JSONObject json = new JSONObject();
                json.put("sender", new JSONObject().put("name", "Tu Nombre").put("email", "tucorreo@tudominio.com"));
                json.put("to", new org.json.JSONArray()
                        .put(new JSONObject().put("email", "destinatario@ejemplo.com").put("name", "Destinatario")));
                json.put("subject", "Correo desde Java con Sendinblue API");
                json.put("htmlContent", "<html><body><h1>Hola desde Java!</h1><p>Este correo fue enviado usando la API de Sendinblue.</p></body></html>");

                // Enviar el POST
                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes("utf-8"));
                os.close();

                int responseCode = con.getResponseCode();
                if (responseCode == 201) {
                    System.out.println("Correo enviado exitosamente.");
                } else {
                    System.out.println("Error al enviar correo. Código: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

 */