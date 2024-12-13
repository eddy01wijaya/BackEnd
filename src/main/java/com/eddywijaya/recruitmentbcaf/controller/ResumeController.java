package com.eddywijaya.recruitmentbcaf.controller;

import com.eddywijaya.recruitmentbcaf.dto.validasi.ValResumeDTO;
import com.eddywijaya.recruitmentbcaf.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    private Map<String,Object> map = new HashMap<>();
    private ModelMapper modelMapper = new ModelMapper();

    public ResumeController() {
        initMap();
    }

    public void initMap() {
        map.clear();
        map.put("mail","email");
        map.put("namalengkap","fullname");
        map.put("tempatlahir", "birthPlace");
        map.put("tanggallahir", "birthDate");
        map.put("namaSekolah", "schoolName");
        map.put("pendidikanTerakhir", "lastEducation");
        map.put("tahunLulus", "graduationYear");
        map.put("ipk", "gpa");
        map.put("nomorIdentitas", "idNumber");
        map.put("jenisKelamin", "gender");
        map.put("agama", "religion");
        map.put("statusPerkawinan", "maritalStatus");
        map.put("alamat", "address");
        map.put("nomorTelepon", "phoneNumber");
        map.put("rekomendasiAI", "aiRecommendation");
        map.put("tahapan", "stages");
    }


    @GetMapping
//    @PreAuthorize("hasAuthority('USER-MAN')")
    public ResponseEntity<Object> getDefault(HttpServletRequest request){
        Pageable pageable =  PageRequest.of(0,10, Sort.by("id"));//ASC
        return resumeService.findAll(pageable,request);
    }

    /** Save */
    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ValResumeDTO valResumeDTO, HttpServletRequest request){
        return resumeService.save(resumeService.convertToEntiy(valResumeDTO),request);
    }

    /** Save */
    @PostMapping("/readOCR")
    public ResponseEntity<Object> readOCR(MultipartFile file, HttpServletRequest request){
        return resumeService.readOCR(file,request);
    }

    /** update */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long aLong
            ,@Valid @RequestBody ValResumeDTO valResumeDTO
            , HttpServletRequest request){
        return resumeService.update(aLong,resumeService.convertToEntiy(valResumeDTO),request);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id, HttpServletRequest request){
        return resumeService.delete(id,request);
    }

    //find by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id,HttpServletRequest request){
        return resumeService.findById(id,request);
    }

    //cek token
    @GetMapping("/token/{token}")
    public ResponseEntity<Object> findByToken(@PathVariable(value = "token") String token){
        return resumeService.validateToken(token);
    }

    //pagination
    //http://localhost:8080/resume/1/asc/name?size=3&col=name&val=2
    //page halaman ke berapa
    //sort asc atau desc
    //sort by kolom apa
    //size per halaman
    //kolom untuk dicari
    //value yang dicari
    @GetMapping("/{page}/{sort}/{sort-by}")
    public ResponseEntity<Object> findByParam(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sort-by") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "col") String column,
            @RequestParam(value = "val") String value,
            HttpServletRequest request
    ){
        Pageable pageable = null;
        sortBy = map.get(sortBy)==null?"id":map.get(sortBy).toString();
        column = map.get(column)==null?"id":map.get(column).toString();
        if("asc".equals(sort)){
            pageable = PageRequest.of(page,size,Sort.by(sortBy));//ASC
        }else{
            pageable = PageRequest.of(page,size,Sort.by(sortBy).descending());//DESC
        }
        return resumeService.findByParam(pageable,column,value,request);
    }

    @GetMapping("/download-sheet")
    public void downloadExcel(
            @RequestParam(value = "col") String column,
            @RequestParam(value = "val") String value,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        column = map.get(column)==null?"id":map.get(column).toString();
        resumeService.downloadReportExcel(column,value,request,response);
    }

    @GetMapping("/download-pdf")
    public void downloadPDF(
            @RequestParam(value = "col") String column,
            @RequestParam(value = "val") String value,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        column = map.get(column)==null?"id":map.get(column).toString();
        resumeService.generateToPDF(column,value,request,response);
    }

    public ResponseEntity<Object> readOCR(@RequestParam("file") MultipartFile file,
                          @RequestParam("additionalInfo") String additionalInfo,
                          HttpServletRequest request) {
        return resumeService.readOCR(file ,request);
    }
}
