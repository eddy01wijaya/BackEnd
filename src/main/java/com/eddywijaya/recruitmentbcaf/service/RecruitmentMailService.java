package com.eddywijaya.recruitmentbcaf.service;

import com.eddywijaya.recruitmentbcaf.config.SMTPConfig;
import com.eddywijaya.recruitmentbcaf.core.IFile;
import com.eddywijaya.recruitmentbcaf.core.IService;
import com.eddywijaya.recruitmentbcaf.dto.response.RespRecruitmentMailDTO;
import com.eddywijaya.recruitmentbcaf.dto.validasi.ValRecruitmentMailDTO;
import com.eddywijaya.recruitmentbcaf.model.Recruitment;
import com.eddywijaya.recruitmentbcaf.model.RecruitmentMail;
import com.eddywijaya.recruitmentbcaf.repo.RecruitmentMailRepo;
import com.eddywijaya.recruitmentbcaf.repo.RecruitmentRepo;
import com.eddywijaya.recruitmentbcaf.security.Crypto;
import com.eddywijaya.recruitmentbcaf.util.ExcelWriter;
import com.eddywijaya.recruitmentbcaf.util.GlobalFunction;
import com.eddywijaya.recruitmentbcaf.util.PdfGenerator;
import com.eddywijaya.recruitmentbcaf.util.TransformPagination;
import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class RecruitmentMailService implements IService<RecruitmentMail> {

    @Autowired
    private RecruitmentMailRepo recruitmentMailRepo;

    @Autowired
    private RecruitmentRepo recruitmentRepo;

    @Autowired
    private RecruitmentService recruitmentService;

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild = new StringBuilder();

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Override
    public ResponseEntity<Object> save(RecruitmentMail recruitmentMail, HttpServletRequest request) {
        return null;
    }

    @Transactional
    public ResponseEntity<Object> save(ValRecruitmentMailDTO valRecruitmentMailDTO, HttpServletRequest request) {
        try {
            Optional<Recruitment> recruitmentOptional = recruitmentRepo.findById(valRecruitmentMailDTO.getIdRecruitment());
            if (recruitmentOptional.isEmpty()) {
                return GlobalFunction.dataGagalDisimpan("FEREC004001", request);
            }

            Recruitment recruitment = recruitmentOptional.get();
            List<RecruitmentMail> recruitmentMails = new ArrayList<>();

            // Loop through each email and save individually
            for (String email : valRecruitmentMailDTO.getEmails()) {
                Optional<RecruitmentMail> recruitmentMailOptional = recruitmentMailRepo.findByEmailAndRecruitments_id(email,valRecruitmentMailDTO.getIdRecruitment());
                if (recruitmentMailOptional.isPresent()) {
                    continue;
                    //return GlobalFunction.dataGagalDisimpan("FEREC004001", request);
                }
                RecruitmentMail recruitmentMail = new RecruitmentMail();
                recruitmentMail.setEmail(email);
                recruitmentMail.setRecruitments(recruitment);
                recruitmentMail.setCreatedBy(1L);
                recruitmentMails.add(recruitmentMail);
                // Kirim email menggunakan SMTP Gmail

                // Buat thread untuk mengirim email
                new Thread(() -> {
                    try {
                        sendEmail(email, "BCAF Recruitment Form - "+recruitment.getRecruitmentName(),
                                "Terima kasih sudah meluangkan waktu untuk melakukan Proses Seleksi Tahap I!\n\n" +
                                        "Selamat! Kamu dinyatakan lolos ke tahap berikutnya!\n\n" +
                                        "Pada tahap ini kamu diminta untuk melakukan pengisian data di link:\n" +
                                        "http://localhost:4200/#/resume?v=" + paramGenerate(valRecruitmentMailDTO.getIdRecruitment().toString(),email) + "\n\n" +
                                        "Berdasarkan pengisian data tersebut, apabila kamu sesuai dengan kualifikasi kami maka selanjutnya akan dihubungi kembali untuk mengikuti proses ke tahap berikutnya. Silakan persiapkan diri sebaik mungkin ya :)\n\n" +
                                        "Good Luck!\n"
                        );
                        recruitmentMailRepo.saveAll(recruitmentMails);
                    } catch (Exception e) {
                        System.err.println("Error saat mengirim email ke: " + email);
                        e.printStackTrace();
                    }
                }).start(); // Memulai thread
            }

            // Save all recruitment mails at once


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return GlobalFunction.dataGagalDisimpan("FEREC004001", request);
        }
        return GlobalFunction.dataBerhasilDisimpan(request);
    }


    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, RecruitmentMail recruitmentMail, HttpServletRequest request) {
        try{
            Optional<RecruitmentMail> optionalRecruitmentMail = recruitmentMailRepo.findById(id);
            if(!optionalRecruitmentMail.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            RecruitmentMail recruitmentMailNext = optionalRecruitmentMail.get();
            recruitmentMailNext.setEmail(recruitmentMail.getEmail());
            recruitmentMailNext.setCode(recruitmentMail.getCode());
            recruitmentMailNext.setRecruitments(recruitmentMail.getRecruitments());
            recruitmentMailNext.setModifiedBy(recruitmentMail.getModifiedBy());
        }catch (Exception e) {
            return GlobalFunction.dataGagalDisimpan("FEAUT004011",request);//011-020
        }
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Transactional
    public ResponseEntity<Object> updateByMail(RecruitmentMail recruitmentMail,String email, Long idRec, HttpServletRequest request) {
        try{
            Optional<RecruitmentMail> optionalRecruitmentMail = recruitmentMailRepo.findByEmailAndRecruitments_id(email, idRec);
            if(!optionalRecruitmentMail.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            RecruitmentMail recruitmentMailNext = optionalRecruitmentMail.get();
            recruitmentMailNext.setEmail(recruitmentMail.getEmail());
            recruitmentMailNext.setCode(recruitmentMail.getCode());
            recruitmentMailNext.setRecruitments(recruitmentMail.getRecruitments());
            recruitmentMailNext.setModifiedBy(recruitmentMail.getModifiedBy());
        }catch (Exception e) {
            return GlobalFunction.dataGagalDisimpan("UpdateByMail",request);//011-020
        }
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<RecruitmentMail> optionalRecruitmentMail = recruitmentMailRepo.findById(id);
            if(!optionalRecruitmentMail.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            recruitmentMailRepo.deleteById(id);
        }catch (Exception e){
            return GlobalFunction.dataGagalDihapus("FEAUT004021",request);//021-030
        }
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<RecruitmentMail> page = null;
        List<RecruitmentMail> list = null;
        page=recruitmentMailRepo.findAll(pageable);
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespRecruitmentMailDTO(list),
                page,
                null,null
        );
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<RecruitmentMail> optionalRecruitmentMail = recruitmentMailRepo.findById(id);
        if(!optionalRecruitmentMail.isPresent()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }
        RecruitmentMail recruitmentMail = optionalRecruitmentMail.get();

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(recruitmentMail, RespRecruitmentMailDTO.class));
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<RecruitmentMail> page = null;
        List<RecruitmentMail> list = null;
        switch (columnName){
            case "email":page=recruitmentMailRepo.findByEmailContainingIgnoreCase(pageable,value);break;
//            case "username":page=recruitmentMailRepo.findByUsernameContainingIgnoreCase(pageable,value);break;
//            case "fullname":page=recruitmentMailRepo.findByFullNameContainingIgnoreCase(pageable,value);break;
            default:page=recruitmentMailRepo.findAll(pageable);
        }
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespRecruitmentMailDTO(list),
                page,
                columnName,value
        );
    }
    

    public List<RespRecruitmentMailDTO> convertToListRespRecruitmentMailDTO(List<RecruitmentMail> recruitmentMails){
        return modelMapper.map(recruitmentMails,new TypeToken<List<RespRecruitmentMailDTO>>(){}.getType());
    }

    public RecruitmentMail convertToEntiy(ValRecruitmentMailDTO valRecruitmentMailDTO){
        return modelMapper.map(valRecruitmentMailDTO, RecruitmentMail.class);
    }

    private void sendEmail(String toEmail, String subject, String body) {
        // Properti SMTP yang diambil dari konfigurasi
        Properties props = new Properties();
        props.put("mail.smtp.auth", SMTPConfig.getEmailAuth());
        props.put("mail.smtp.starttls.enable", SMTPConfig.getEmailStartTLSEnable());
        props.put("mail.smtp.host", SMTPConfig.getEmailHost());
        props.put("mail.smtp.port", SMTPConfig.getEmailPortTLS());
        props.put("mail.smtp.socketFactory.class", SMTPConfig.getEmailSMTPSocketFactoryClass());
//        props.put("mail.smtp.timeout", SMTPConfig.ge;

        // Membuat session dengan autentikasi
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTPConfig.getEmailUserName(), SMTPConfig.getEmailPassword());
            }
        });

        try {
            // Membuat pesan email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTPConfig.getEmailUserName())); // Email pengirim
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Kirim email
            Transport.send(message);

            System.out.println("Email sent to: " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    // Method untuk generate param dan kode
    public String paramGenerate(String idRecruitment, String email) {
        String code = generateRandomCode();
        return Crypto.performEncrypt(idRecruitment+"##"+code+"##"+ email);
    }

    // Method untuk generate kode acak 6 digit dari 000000 sampai 999999
    private String generateRandomCode() {
        Random random = new Random();
        int code = random.nextInt(1000000); // 1000000 untuk batas atas exclusive (0 - 999999)
        return String.format("%06d", code);
    }

}
