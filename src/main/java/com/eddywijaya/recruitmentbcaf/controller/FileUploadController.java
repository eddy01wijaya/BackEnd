//package com.eddywijaya.recruitmentbcaf.controller;
//
//import io.micrometer.core.instrument.util.StringEscapeUtils;
//import org.apache.tika.Tika;
//import org.apache.tika.exception.TikaException;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.Files;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Map;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:4200")
//@RequestMapping("/upload")
//public class FileUploadController {
//
//    private final Tika tika;
//    @Value("${google.api.key}")
//    private String googleApiKey;
//
//
//    // Tentukan lokasi penyimpanan file
//    private final String uploadDir = "uploaded_files/"; // Ganti dengan path sesuai kebutuhan Anda
//
//    public FileUploadController() {
//        this.tika = new Tika();
//
//        // Membuat direktori jika belum ada
//        Files directory = new Files(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty() || file.getSize() > 2 * 1024 * 1024) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid file or file size exceeds 2MB"));
//        }
//
//        try {
//            // Simpan file ke server
//            Path filePath = Paths.get(uploadDir + file.getOriginalFilename());
//            Files.write(filePath, file.getBytes());
//
//            // Perform OCR with Tika
//            String ocrText = tika.parseToString(file.getInputStream());
//
//            // Call Gemini API with OCR result
//            //idkoneksi get dulu, create by method
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String geminiResponse = callGeminiApi(ocrText);
//                    // Proses respons Gemini untuk mendapatkan teks
//                    String cleanResponse = processGeminiResponse(geminiResponse);
//                    wsService.notifyUser();
//                }
//            }).start();
//
//
//            // Proses respons Gemini untuk mendapatkan teks
////            String cleanResponse = processGeminiResponse(geminiResponse);
//
//            // Kembalikan respons
//            Map<String, String> response = Map.of(
//                    "filePath", filePath.toString(),
//                    "ocrText", ocrText,
//                    "geminiResponse", cleanResponse
//            );
//
//            // Hapus file setelah mendapatkan respons
//            deleteFile(filePath);
//
//            return ResponseEntity.ok(response);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Files upload or OCR failed"));
//        } catch (TikaException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String processGeminiResponse(String geminiResponse) {
//        try {
//            // Parsing JSON menggunakan org.json
//            JSONObject responseObject = new JSONObject(geminiResponse);
//            JSONArray candidates = responseObject.getJSONArray("candidates");
//
//            // Memeriksa apakah ada kandidat
//            if (candidates.length() > 0) {
//                JSONObject firstCandidate = candidates.getJSONObject(0);
//                JSONObject content = firstCandidate.getJSONObject("content");
//                JSONArray parts = content.getJSONArray("parts");
//
//                // Memeriksa apakah ada bagian dalam konten
//                if (parts.length() > 0) {
//                    String text = parts.getJSONObject(0).getString("text");
//
//                    // Menghapus kode Markdown (```json\n...\n```) dari teks
//                    text = text.replaceAll("```json\\n|\\n```", "");
//                    return text; // Kembalikan teks yang bersih
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Anda bisa mengembalikan pesan error atau nilai default jika parsing gagal
//        }
//
//        return null; // Kembalikan null jika tidak ada teks yang ditemukan
//    }
//
//
//    private void deleteFile(Path filePath) {
//        try {
//            Files.delete(filePath);
//            System.out.println("Files deleted: " + filePath);
//        } catch (IOException e) {
//            System.err.println("Failed to delete file: " + filePath);
//            e.printStackTrace();
//        }
//    }
//
//    private String callGeminiApi(String ocrText) {
//        if(ocrText.isEmpty())
//            return null;
//        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + googleApiKey;
//
//        // Escape karakter khusus dalam OCR text
//        String escapedOcrText = StringEscapeUtils.escapeJson(ocrText);
//        ocrText = "Buat jadi json(jawab hasil json saja) dengan kolom "+
//                "FullName,BirthPlace,BirthDate,SchoolName,LastEducation,GraduationYear,GPA, IDNumber,Gender,BirthDate,Religion,MaritalStatus,Address,PhoneNumber" +
//                "dari string berikut, jika tidal ada maka null:"
//                +escapedOcrText;
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//
//        String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + ocrText + "\" }] }] }";
//        //System.out.println(requestBody);
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//        return response.getBody();
//    }
//}
//
