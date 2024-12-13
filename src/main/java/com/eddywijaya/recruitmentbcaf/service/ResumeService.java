package com.eddywijaya.recruitmentbcaf.service;

import com.eddywijaya.recruitmentbcaf.core.IFile;
import com.eddywijaya.recruitmentbcaf.core.IService;
import com.eddywijaya.recruitmentbcaf.dto.response.RespResumeDTO;
import com.eddywijaya.recruitmentbcaf.dto.validasi.ValResumeDTO;
import com.eddywijaya.recruitmentbcaf.model.Recruitment;
import com.eddywijaya.recruitmentbcaf.model.RecruitmentMail;
import com.eddywijaya.recruitmentbcaf.model.Resume;
import com.eddywijaya.recruitmentbcaf.model.Stages;
import com.eddywijaya.recruitmentbcaf.repo.RecruitmentMailRepo;
import com.eddywijaya.recruitmentbcaf.repo.RecruitmentRepo;
import com.eddywijaya.recruitmentbcaf.repo.ResumeRepo;
import com.eddywijaya.recruitmentbcaf.repo.StagesRepo;
import com.eddywijaya.recruitmentbcaf.security.Crypto;
import com.eddywijaya.recruitmentbcaf.util.ExcelWriter;
import com.eddywijaya.recruitmentbcaf.util.GlobalFunction;
import com.eddywijaya.recruitmentbcaf.util.PdfGenerator;
import com.eddywijaya.recruitmentbcaf.util.TransformPagination;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class ResumeService implements IService<Resume>, IFile<Resume> {

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private RecruitmentRepo recruitmentRepo;

    @Autowired
    private RecruitmentMailRepo recruitmentMailRepo;

    @Autowired
    private StagesRepo stagesRepo;



    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild = new StringBuilder();

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Override
    @Transactional
    public ResponseEntity<Object> save(Resume resume, HttpServletRequest request) {
        try{
            String value = request.getHeader("Value");
            value = Crypto.performDecrypt(value);
            String[] parts = value.split("##");
            Long id = Long.parseLong(parts[0]);
            String email = parts[2];
            Optional<Recruitment> parsedValue = recruitmentRepo.findById(id);
            Optional<RecruitmentMail> rmp =
            recruitmentMailRepo.findByEmailAndRecruitments_id(email, id);
            if(parsedValue.isEmpty() || rmp.isEmpty() || rmp.get().getCode() != null && !rmp.get().getCode().isEmpty()){
                return GlobalFunction.dataGagalDisimpan("FEREC004001-1",request);
            }
            Recruitment recruitment = parsedValue.get();
            resume.setRecruitment(recruitment);
            List<Stages> stagesList = recruitment.getStages();
            if (stagesList == null || stagesList.isEmpty()) {
                return GlobalFunction.dataGagalDisimpan("FEREC004001-7", request); // List kosong
            }
            Stages firstStage = stagesList.get(0);
            resume.setStages(firstStage);
            resume.setCreatedBy(0L);
            rmp.get().setCode(parts[1]);
            //recruitmentMailRepo.save(rmp.get());
            resumeRepo.save(resume);
        }catch (Exception e) {
            System.out.println("error"+e.getMessage());
            return GlobalFunction.dataGagalDisimpan("FEREC004001",request);
        }
        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, Resume resume, HttpServletRequest request) {
        try{
            Optional<Resume> optionalResume = resumeRepo.findById(id);
            if(!optionalResume.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            Resume resumeNext = optionalResume.get();
            Stages stages = stagesRepo.findById(resume.getStages().getId()).orElseThrow(() -> new IllegalArgumentException("Stage not found"));
            resumeNext.setBirthPlace(resume.getBirthPlace());
            resumeNext.setBirthDate(resume.getBirthDate());
            resumeNext.setSchoolName(resume.getSchoolName());
            resumeNext.setLastEducation(resume.getLastEducation());
            resumeNext.setGraduationYear(resume.getGraduationYear());
            resumeNext.setGpa(resume.getGpa());
            resumeNext.setIdNumber(resume.getIdNumber());
            resumeNext.setGender(resume.getGender());
            resumeNext.setReligion(resume.getReligion());
            resumeNext.setMaritalStatus(resume.getMaritalStatus());
            resumeNext.setAddress(resume.getAddress());
            resumeNext.setPhoneNumber(resume.getPhoneNumber());
            resumeNext.setAiRecommendation(resume.getAiRecommendation());
            resumeNext.setStages(stages);
            resumeNext.setModifiedBy(resume.getModifiedBy());
            resumeNext.setActive(resume.getActive());

        }catch (Exception e) {
            return GlobalFunction.dataGagalDisimpan("FEAUT004011",request);//011-020
        }
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<Resume> optionalResume = resumeRepo.findById(id);
            if(!optionalResume.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            resumeRepo.deleteById(id);
        }catch (Exception e){
            return GlobalFunction.dataGagalDihapus("FEAUT004021",request);//021-030
        }
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<Resume> page = null;
        List<Resume> list = null;
        page=resumeRepo.findAll(pageable);
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespResumeDTO(list),
                page,
                null,null
        );
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Resume> optionalResume = resumeRepo.findById(id);
        if(!optionalResume.isPresent()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }
        Resume resume = optionalResume.get();

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(resume, RespResumeDTO.class));
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Resume> page = null;
        List<Resume> list = null;
        switch (columnName) {
            case "birth-place":
                page = resumeRepo.findByBirthPlaceContainingIgnoreCase(pageable, value);
                break;
            case "birth-date":
                page = resumeRepo.findByBirthDate(pageable, LocalDate.parse(value));
                break;
            case "school-name":
                page = resumeRepo.findBySchoolNameContainingIgnoreCase(pageable, value);
                break;
            case "last-education":
                page = resumeRepo.findByLastEducationContainingIgnoreCase(pageable, value);
                break;
            case "graduation-year":
                page = resumeRepo.findByGraduationYear(pageable, Integer.parseInt(value));
                break;
            case "gpa":
                page = resumeRepo.findByGpa(pageable, Double.parseDouble(value));
                break;
            case "id-number":
                page = resumeRepo.findByIdNumberContainingIgnoreCase(pageable, value);
                break;
            case "gender":
                page = resumeRepo.findByGenderContainingIgnoreCase(pageable, value);
                break;
            case "religion":
                page = resumeRepo.findByReligionContainingIgnoreCase(pageable, value);
                break;
            case "marital-status":
                page = resumeRepo.findByMaritalStatusContainingIgnoreCase(pageable, value);
                break;
            case "address":
                page = resumeRepo.findByAddressContainingIgnoreCase(pageable, value);
                break;
            case "phone-number":
                page = resumeRepo.findByPhoneNumberContainingIgnoreCase(pageable, value);
                break;
            case "ai-recommendation":
                page = resumeRepo.findByAiRecommendationContainingIgnoreCase(pageable, value);
                break;
            case "is-active":
                page = resumeRepo.findByIsActive(pageable, Boolean.parseBoolean(value));
                break;
            case "stages":
                String[] values = value.split("\\.");
                if (values.length == 2) {
                    Integer idStages = Integer.parseInt(values[0]);
                    Integer idRecruitment = Integer.parseInt(values[1]);
                    page = resumeRepo.findByStages_IdAndRecruitment_Id(pageable, idStages, idRecruitment);
                } else {
                    throw new IllegalArgumentException("Format nilai tidak valid, harus berupa idStages.idRecruitment");
                }
                break;
            default:
                page = resumeRepo.findAll(pageable);
                break;
        }

        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespResumeDTO(list),
                page,
                columnName,value
        );
    }


    @Override
    public String uploadFile(MultipartFile multipartFile, HttpServletRequest request) {
        return "";
    }



    @Override
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List<Resume> resumeList = null;
        switch (column) {
            case "birth-place":
                resumeList = resumeRepo.findByBirthPlaceContainingIgnoreCase(value);
                break;
            case "birth-date":
                resumeList = resumeRepo.findByBirthDate(LocalDate.parse(value));
                break;
            case "school-name":
                resumeList = resumeRepo.findBySchoolNameContainingIgnoreCase(value);
                break;
            case "last-education":
                resumeList = resumeRepo.findByLastEducationContainingIgnoreCase(value);
                break;
            case "graduation-year":
                resumeList = resumeRepo.findByGraduationYear(Integer.parseInt(value));
                break;
            case "gpa":
                resumeList = resumeRepo.findByGpa(Double.parseDouble(value));
                break;
            case "id-number":
                resumeList = resumeRepo.findByIdNumberContainingIgnoreCase(value);
                break;
            case "gender":
                resumeList = resumeRepo.findByGenderContainingIgnoreCase(value);
                break;
            case "religion":
                resumeList = resumeRepo.findByReligionContainingIgnoreCase(value);
                break;
            case "marital-status":
                resumeList = resumeRepo.findByMaritalStatusContainingIgnoreCase(value);
                break;
            case "address":
                resumeList = resumeRepo.findByAddressContainingIgnoreCase(value);
                break;
            case "phone-number":
                resumeList = resumeRepo.findByPhoneNumberContainingIgnoreCase(value);
                break;
            case "ai-recommendation":
                resumeList = resumeRepo.findByAiRecommendationContainingIgnoreCase(value);
                break;
            case "is-active":
                resumeList = resumeRepo.findByIsActive(Boolean.parseBoolean(value));
                break;
            default:
                resumeList = resumeRepo.findAll();
                break;
        }

        List<RespResumeDTO> listRespResume = convertToListRespResumeDTO(resumeList);
        if (listRespResume.isEmpty()) {
//            GlobalFunction.manualResponse(response,GlobalFunction.dataTidakDitemukan(request));
            return;
        }
        sBuild.setLength(0);
        String headerKey = "Content-Disposition";
        sBuild.setLength(0);

        String headerValue = sBuild.append("attachment; filename=resume_").
                append( new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss.SSS").format(new Date())).//audit trails lewat nama file nya
                        append(".xlsx").toString();//buat excel
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");

        Map<String,Object> map = GlobalFunction.convertClassToObject(new RespResumeDTO());
        List<String> listTampungSebentar = new ArrayList<>();
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            listTampungSebentar.add(entry.getKey());
        }
        int intListTampungSebentar = listTampungSebentar.size();
        String [] headerArr = new String[intListTampungSebentar];
        String [] loopDataArr = new String[intListTampungSebentar];
        for (int i = 0; i < intListTampungSebentar; i++) {
            headerArr[i] = GlobalFunction.camelToStandar(String.valueOf(listTampungSebentar.get(i))).toUpperCase();//BIASANYA JUDUL KOLOM DIBUAT HURUF BESAR DENGAN FORMAT STANDARD
            loopDataArr[i] = listTampungSebentar.get(i);
        }
        String[][] strBody = new String[listRespResume.size()][intListTampungSebentar];
        for (int i = 0; i < listRespResume.size(); i++) {
            map = GlobalFunction.convertClassToObject(listRespResume.get(i));
            for (int j = 0; j < intListTampungSebentar; j++) {
                strBody[i][j] = String.valueOf(map.get(loopDataArr[j]));
            }
        }
        new ExcelWriter(strBody, headerArr,"sheet-1", response);
    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List<Resume> resumeList = null;
//        Map<String,Object> payloadJwt = GlobalFunction.claimsTokenBody(request);
        switch (column) {
//            case "name": resumeList = resumeRepo.findByResumeNameContainingIgnoreCase(value);break;

            default:
                resumeList = resumeRepo.findAll();
        }
        List<RespResumeDTO> listRespMenu = convertToListRespResumeDTO(resumeList);
        if (listRespMenu.isEmpty()) {
//            GlobalFunction.manualResponse(response,GlobalFunction.dataTidakDitemukan(request));
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String strHtml = null;
        Context context = new Context();
        Map<String, Object> mapColumnName = GlobalFunction.convertClassToObject(new RespResumeDTO());
        List<String> listTampungSebentar = new ArrayList<>();
        List<String> listHelper = new ArrayList<>();//untuk mapping otomatis di html nya
        for (Map.Entry<String, Object> entry : mapColumnName.entrySet()) {
            listTampungSebentar.add(GlobalFunction.camelToStandar(entry.getKey()));
            listHelper.add(entry.getKey());
        }
        Map<String, Object> mapTampung = null;
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 0; i < listRespMenu.size(); i++) {
            mapTampung = GlobalFunction.convertClassToObject(listRespMenu.get(i), null);
            listMap.add(mapTampung);
        }
        map.put("listKolom",listTampungSebentar);
        map.put("listContent",listMap);
        map.put("listHelper",listHelper);
        map.put("timestamp",GlobalFunction.formatingDateDDMMMMYYYY());
        map.put("username","Edd");
        map.put("totalData",listRespMenu.size());
        map.put("title","REPORT Resume");
        context.setVariables(map);
        strHtml = springTemplateEngine.process("global-report",context);
        pdfGenerator.htmlToPdf(strHtml,"Resumes",response);
    }

    public List<RespResumeDTO> convertToListRespResumeDTO(List<Resume> resumes){
        return modelMapper.map(resumes,new TypeToken<List<RespResumeDTO>>(){}.getType());
    }

    public Resume convertToEntiy(ValResumeDTO valResumeDTO){
        return modelMapper.map(valResumeDTO, Resume.class);
    }


    private final Tika tika;
    private final String uploadDir = "uploaded_files/"; // Lokasi penyimpanan file

    @Value("${google.api.key}")
    private String googleApiKey;

    public ResumeService() {
        this.tika = new Tika();
        // Membuat direktori jika belum ada
        try {
            Path directory = Paths.get(uploadDir);
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<Object> readOCR(MultipartFile file, HttpServletRequest request) {
        String value = request.getHeader("Value");
        value = Crypto.performDecrypt(value);
        String[] parts = value.split("##");
        Long id = Long.parseLong(parts[0]);
        Optional<Recruitment> parsedValue = recruitmentRepo.findById(id);

        Map<String, String> response = new HashMap<>();

        if (file.isEmpty() || file.getSize() > 2 * 1024 * 1024) {
            response.put("error", "Invalid file or file size exceeds 2MB");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // Simpan file ke server
            Path filePath = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            // Perform OCR with Tika
            String ocrText = tika.parseToString(file.getInputStream());

            // Call Gemini API with OCR result
            String geminiResponse = callGeminiApi(ocrText, parsedValue.get().getRequirements());
            String cleanResponse = processGeminiResponse(geminiResponse);

            // Kembalikan respons
            response.put("filePath", filePath.toString());
            response.put("ocrText", ocrText);
            response.put("geminiResponse", cleanResponse);

        } catch (IOException | TikaException e) {
            e.printStackTrace();
            response.put("error", "File upload or OCR failed");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public String callGeminiApi(String ocrText, String requirements) {
        if (ocrText.isEmpty()) {
            return null;
        }
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + googleApiKey;

        // Escape karakter khusus dalam OCR text
        String escapedOcrText = StringEscapeUtils.escapeJson(ocrText);
        ocrText = "Buat jadi json(jawab hasil json saja) dengan kolom " +
                "email, fullName, birthPlace, birthDate, schoolName, lastEducation, graduationYear, gpa, idNumber, gender, religion, maritalStatus, address, phoneNumber, aiRecommendation" +
                "dari string berikut, jika tidak ada maka null:" + escapedOcrText +
        "untuk aiRecommendation kamu bandingkan data tersebut dengan ini :"+requirements+", misal ada ketidaksesuaian jawab (Tidak sesuai,alasan) kalau sesuai hanya jawab (Sesuai)";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + ocrText + "\" }] }] }";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String processGeminiResponse(String geminiResponse) {
        try {
            JSONObject responseObject = new JSONObject(geminiResponse);
            JSONArray candidates = responseObject.getJSONArray("candidates");

            if (candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");

                if (parts.length() > 0) {
                    String text = parts.getJSONObject(0).getString("text");
                    text = text.replaceAll("```json\\n|\\n```", "");
                    return text;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResponseEntity<Object> validateToken(String token){
        Map<String, String> response = new HashMap<>();
        String value = Crypto.performDecrypt(token);
        String[] parts = value.split("##");
        Long id = Long.parseLong(parts[0]);
        String email = parts[2];


        Optional<Recruitment> recruitment = recruitmentRepo.findById(id);
        // Jika Recruitment tidak ditemukan atau closeDate sudah terisi, anggap link invalid
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate closeDate = recruitment.get().getCloseDate();
        if (!recruitment.isPresent() || recruitment.get().getCloseDate().isBefore(yesterday)) {
            response.put("invalid", "Link sudah tidak valid atau rekrutmen telah ditutup.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        Optional<RecruitmentMail> recruitmentMail = recruitmentMailRepo.findByEmailAndRecruitments_id(email,id);

        if (recruitmentMail.filter(mail -> mail.getCode() != null && !mail.getCode().isEmpty()).isPresent()) {
            response.put("invalid", "Link sudah pernah disubmit oleh email " + email);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response.put("valid", "Link valid");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
