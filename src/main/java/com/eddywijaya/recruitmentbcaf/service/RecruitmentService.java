package com.eddywijaya.recruitmentbcaf.service;

import com.eddywijaya.recruitmentbcaf.core.IFile;
import com.eddywijaya.recruitmentbcaf.core.IService;
import com.eddywijaya.recruitmentbcaf.dto.response.RespRecruitmentDTO;
import com.eddywijaya.recruitmentbcaf.dto.response.RespResumeDTO;
import com.eddywijaya.recruitmentbcaf.dto.validasi.ValRecruitmentDTO;
import com.eddywijaya.recruitmentbcaf.model.Recruitment;
import com.eddywijaya.recruitmentbcaf.model.Stages;
import com.eddywijaya.recruitmentbcaf.repo.RecruitmentRepo;
import com.eddywijaya.recruitmentbcaf.repo.StagesRepo;
import com.eddywijaya.recruitmentbcaf.util.ExcelWriter;
import com.eddywijaya.recruitmentbcaf.util.GlobalFunction;
import com.eddywijaya.recruitmentbcaf.util.PdfGenerator;
import com.eddywijaya.recruitmentbcaf.util.TransformPagination;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class RecruitmentService implements IService<Recruitment>, IFile<Recruitment> {

    @Autowired
    private RecruitmentRepo recruitmentRepo;

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
    public ResponseEntity<Object> save(Recruitment recruitment, HttpServletRequest request) {
        try{
            //$2a$11$.HryliSpcSd/8Ml3nOSwI.KAMXle9Vfn9JcVrzghOpSRR7AHGEnNu
//            recruitment.setPassword(BcryptImpl.hash(recruitment.getPassword()+recruitment.getRecruitmentname()));
            List<Stages> allStages = stagesRepo.findAll();
            recruitment.setApproval('P');
            recruitment.setCreatedBy(1L);
            recruitment.setStages(allStages);
            recruitmentRepo.save(recruitment);
        }catch (Exception e) {
            System.out.println("error"+e.getMessage());
            return GlobalFunction.dataGagalDisimpan("FEREC004001",request);
        }
        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, Recruitment recruitment, HttpServletRequest request) {
        try{
            Optional<Recruitment> optionalRecruitment = recruitmentRepo.findById(id);
            if(!optionalRecruitment.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            Recruitment recruitmentNext = optionalRecruitment.get();
            recruitmentNext.setRecruitmentName(recruitment.getRecruitmentName());
            recruitmentNext.setRequirements(recruitment.getRequirements());
            recruitmentNext.setOpenDate(recruitment.getOpenDate());
            recruitmentNext.setCloseDate(recruitment.getCloseDate());
            recruitmentNext.setModifiedBy(recruitment.getModifiedBy());
            recruitmentNext.setApproval(recruitment.getApproval());
        }catch (Exception e) {
            return GlobalFunction.dataGagalDisimpan("FEAUT004011",request);//011-020
        }
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<Recruitment> optionalRecruitment = recruitmentRepo.findById(id);
            if(!optionalRecruitment.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            recruitmentRepo.deleteById(id);
        }catch (Exception e){
            return GlobalFunction.dataGagalDihapus("FEAUT004021",request);//021-030
        }
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<Recruitment> page = null;
        List<Recruitment> list = null;
        page=recruitmentRepo.findAll(pageable);
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespRecruitmentDTO(list),
                page,
                null,null
        );
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Recruitment> optionalRecruitment = recruitmentRepo.findById(id);
        if(!optionalRecruitment.isPresent()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }
        Recruitment recruitment = optionalRecruitment.get();

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(recruitment, RespRecruitmentDTO.class));
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Recruitment> page = null;
        List<Recruitment> list = null;
        switch (columnName){
            case "name":page=recruitmentRepo.findByRecruitmentNameContainingIgnoreCase(pageable,value);break;
            case "requirements":page=recruitmentRepo.findByRequirementsContainingIgnoreCase(pageable,value);break;
            case "open-date":page=recruitmentRepo.findByOpenDate(pageable,LocalDate.parse(value));break;
            case "close-date":page=recruitmentRepo.findByCloseDate(pageable,LocalDate.parse(value));break;
            case "approval":char charValue = value.charAt(0);  // Convert the first character of the string 'value' to char
                page = recruitmentRepo.findByApproval(pageable, charValue);
                break;
            default:page=recruitmentRepo.findAll(pageable);
        }
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespRecruitmentDTO(list),
                page,
                columnName,value
        );
    }


    @Override
    public String uploadFile(MultipartFile multipartFile, HttpServletRequest request) {
        return "";
    }

    @Override
    public ResponseEntity<Object> readOCR(MultipartFile multipartFile, HttpServletRequest request) {
        return null;
    }


    @Override
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List<Recruitment> recruitmentList = null;
        switch (column){
            case "alamat":recruitmentList=recruitmentRepo.findByRecruitmentNameContainingIgnoreCase(value);break;
            case "name":recruitmentList=recruitmentRepo.findByRecruitmentNameContainingIgnoreCase(value);break;
            case "requirements":recruitmentList=recruitmentRepo.findByRequirementsContainingIgnoreCase(value);break;
            case "open-date":recruitmentList=recruitmentRepo.findByOpenDate(LocalDate.parse(value));break;
            case "close-date":recruitmentList=recruitmentRepo.findByCloseDate(LocalDate.parse(value));break;
            default:recruitmentList=recruitmentRepo.findAll();
        }
        List<RespRecruitmentDTO> listRespRecruitment = convertToListRespRecruitmentDTO(recruitmentList);
        if (listRespRecruitment.isEmpty()) {
//            GlobalFunction.manualResponse(response,GlobalFunction.dataTidakDitemukan(request));
            return;
        }
        sBuild.setLength(0);
        String headerKey = "Content-Disposition";
        sBuild.setLength(0);

        String headerValue = sBuild.append("attachment; filename=recruitment_").
                append( new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss.SSS").format(new Date())).//audit trails lewat nama file nya
                        append(".xlsx").toString();//buat excel
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");

        Map<String,Object> map = GlobalFunction.convertClassToObject(new RespRecruitmentDTO());
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
        String[][] strBody = new String[listRespRecruitment.size()][intListTampungSebentar];
        for (int i = 0; i < listRespRecruitment.size(); i++) {
            map = GlobalFunction.convertClassToObject(listRespRecruitment.get(i));
            for (int j = 0; j < intListTampungSebentar; j++) {
                strBody[i][j] = String.valueOf(map.get(loopDataArr[j]));
            }
        }
        new ExcelWriter(strBody, headerArr,"sheet-1", response);
    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List<Recruitment> recruitmentList = null;
//        Map<String,Object> payloadJwt = GlobalFunction.claimsTokenBody(request);
        switch (column) {
            case "name": recruitmentList = recruitmentRepo.findByRecruitmentNameContainingIgnoreCase(value);break;

            default:
                recruitmentList = recruitmentRepo.findAll();
        }
        List<RespRecruitmentDTO> listRespMenu = convertToListRespRecruitmentDTO(recruitmentList);
        if (listRespMenu.isEmpty()) {
//            GlobalFunction.manualResponse(response,GlobalFunction.dataTidakDitemukan(request));
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String strHtml = null;
        Context context = new Context();
        Map<String, Object> mapColumnName = GlobalFunction.convertClassToObject(new RespRecruitmentDTO());
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
        map.put("title","REPORT Recruitment");
        context.setVariables(map);
        strHtml = springTemplateEngine.process("global-report",context);
        pdfGenerator.htmlToPdf(strHtml,"Recruitments",response);
    }

    public List<RespRecruitmentDTO> convertToListRespRecruitmentDTO(List<Recruitment> recruitments){
        return modelMapper.map(recruitments,new TypeToken<List<RespRecruitmentDTO>>(){}.getType());
    }

    public Recruitment convertToEntiy(ValRecruitmentDTO valRecruitmentDTO){
        return modelMapper.map(valRecruitmentDTO, Recruitment.class);
    }

}
