package com.eddywijaya.recruitmentbcaf.controller;

import com.eddywijaya.recruitmentbcaf.dto.validasi.ValRecruitmentMailDTO;
import com.eddywijaya.recruitmentbcaf.service.RecruitmentMailService;
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
@RequestMapping("recruitmentMail")
public class RecruitmentMailController {

    @Autowired
    private RecruitmentMailService recruitmentMailService;

    private Map<String,Object> map = new HashMap<>();
    private ModelMapper modelMapper = new ModelMapper();

    public RecruitmentMailController() {
        initMap();
    }

    public void initMap() {
        map.clear();
        map.put("mail","email");
    }


    @GetMapping
//    @PreAuthorize("hasAuthority('USER-MAN')")
    public ResponseEntity<Object> getDefault(HttpServletRequest request){
        Pageable pageable =  PageRequest.of(0,10, Sort.by("id"));//ASC
        return recruitmentMailService.findAll(pageable,request);
    }

    /** Save */
    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ValRecruitmentMailDTO valRecruitmentMailDTO, HttpServletRequest request){
        return recruitmentMailService.save(valRecruitmentMailDTO,request);
    }


    /** update */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long aLong
            ,@Valid @RequestBody ValRecruitmentMailDTO valRecruitmentMailDTO
            , HttpServletRequest request){

        // Membaca header "Author" dari HttpServletRequest
//        String authorHeader = request.getHeader("Author");

        // Cetak nilai header "Author" ke konsol
//        System.out.println("Author Header: " + authorHeader);
        return recruitmentMailService.update(aLong,recruitmentMailService.convertToEntiy(valRecruitmentMailDTO),request);
    }

    /** update by mail*/
    @PutMapping
    public ResponseEntity<Object> update(
            @Valid @RequestBody ValRecruitmentMailDTO valRecruitmentMailDTO,
            @RequestParam(value = "mail") String email,
            @RequestParam(value = "id-rec") Long idRecruitment
            , HttpServletRequest request){
        return recruitmentMailService.updateByMail(recruitmentMailService.convertToEntiy(valRecruitmentMailDTO),email,idRecruitment,request);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id, HttpServletRequest request){
        return recruitmentMailService.delete(id,request);
    }

    //find by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id,HttpServletRequest request){
        return recruitmentMailService.findById(id,request);
    }

    //pagination
    //http://localhost:8080/recruitmentMail/1/asc/name?size=3&col=name&val=2
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
        return recruitmentMailService.findByParam(pageable,column,value,request);
    }
}
