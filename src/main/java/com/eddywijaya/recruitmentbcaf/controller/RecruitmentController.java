package com.eddywijaya.recruitmentbcaf.controller;

import com.eddywijaya.recruitmentbcaf.dto.validasi.ValRecruitmentDTO;
import com.eddywijaya.recruitmentbcaf.model.Recruitment;
import com.eddywijaya.recruitmentbcaf.model.Stages;
import com.eddywijaya.recruitmentbcaf.service.RecruitmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("recruitment")
public class RecruitmentController {

    @Autowired
    private RecruitmentService recruitmentService;

    private Map<String,Object> map = new HashMap<>();
    private ModelMapper modelMapper = new ModelMapper();

    public RecruitmentController() {
        initMap();
    }

    public void initMap(){
        map.clear();
        map.put("nama","recruitmentName");
        map.put("syarat","requirements");
        map.put("tanggalbuka","openDate");
        map.put("tanggaltutup","closeDate");
        map.put("persetujuan","approval");

    }

    @GetMapping
//    @PreAuthorize("hasAuthority('USER-MAN')")
    public ResponseEntity<Object> getDefault(HttpServletRequest request){
        Pageable pageable =  PageRequest.of(0,10, Sort.by("id"));//ASC
        return recruitmentService.findAll(pageable,request);
    }

    /** Save */
    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ValRecruitmentDTO valRecruitmentDTO, HttpServletRequest request){
        return recruitmentService.save(recruitmentService.convertToEntiy(valRecruitmentDTO),request);
    }

    /** update */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long aLong
            ,@Valid @RequestBody ValRecruitmentDTO valRecruitmentDTO
            , HttpServletRequest request){

        // Membaca header "Author" dari HttpServletRequest
//        String authorHeader = request.getHeader("Author");

        // Cetak nilai header "Author" ke konsol
//        System.out.println("Author Header: " + authorHeader);
        return recruitmentService.update(aLong,recruitmentService.convertToEntiy(valRecruitmentDTO),request);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id, HttpServletRequest request){
        return recruitmentService.delete(id,request);
    }

    //find by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id,HttpServletRequest request){
        return recruitmentService.findById(id,request);
    }

    //pagination
    //http://localhost:8080/recruitment/1/asc/name?size=3&col=name&val=2
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
        return recruitmentService.findByParam(pageable,column,value,request);
    }

    @GetMapping("/download-sheet")
    public void downloadExcel(
            @RequestParam(value = "col") String column,
            @RequestParam(value = "val") String value,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        column = map.get(column)==null?"id":map.get(column).toString();
        recruitmentService.downloadReportExcel(column,value,request,response);
    }

    @GetMapping("/download-pdf")
    public void downloadPDF(
            @RequestParam(value = "col") String column,
            @RequestParam(value = "val") String value,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        column = map.get(column)==null?"id":map.get(column).toString();
        recruitmentService.generateToPDF(column,value,request,response);
    }
}
