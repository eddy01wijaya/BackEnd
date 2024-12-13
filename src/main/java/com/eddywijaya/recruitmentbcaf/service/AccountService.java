package com.eddywijaya.recruitmentbcaf.service;

import com.eddywijaya.recruitmentbcaf.core.IFile;
import com.eddywijaya.recruitmentbcaf.core.IService;
import com.eddywijaya.recruitmentbcaf.dto.response.RespAccountDTO;
import com.eddywijaya.recruitmentbcaf.dto.validasi.ValAccountDTO;
import com.eddywijaya.recruitmentbcaf.model.Account;
import com.eddywijaya.recruitmentbcaf.repo.AccountRepo;
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
public class AccountService implements IService<Account>, IFile<Account> {

    @Autowired
    private AccountRepo accountRepo;

    private TransformPagination transformPagination = new TransformPagination();
    private ModelMapper modelMapper = new ModelMapper();
    private StringBuilder sBuild = new StringBuilder();

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Override
    public ResponseEntity<Object> save(Account account, HttpServletRequest request) {
        try{
            //$2a$11$.HryliSpcSd/8Ml3nOSwI.KAMXle9Vfn9JcVrzghOpSRR7AHGEnNu
//            account.setPassword(BcryptImpl.hash(account.getPassword()+account.getAccountname()));
            account.setCreatedBy(1L);
            accountRepo.save(account);
        }catch (Exception e) {
            System.out.println("error"+e.getMessage());
            return GlobalFunction.dataGagalDisimpan("FEREC004001",request);
        }
        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, Account account, HttpServletRequest request) {
        try{
            Optional<Account> optionalAccount = accountRepo.findById(id);
            if(!optionalAccount.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            Account accountNext = optionalAccount.get();
            accountNext.setEmail(account.getEmail());
            accountNext.setUsername(account.getUsername());
            accountNext.setFullName(account.getFullName());
            accountNext.setModifiedBy(account.getModifiedBy());
        }catch (Exception e) {
            return GlobalFunction.dataGagalDisimpan("FEAUT004011",request);//011-020
        }
        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<Account> optionalAccount = accountRepo.findById(id);
            if(!optionalAccount.isPresent()) {
                return GlobalFunction.dataTidakDitemukan(request);
            }
            accountRepo.deleteById(id);
        }catch (Exception e){
            return GlobalFunction.dataGagalDihapus("FEAUT004021",request);//021-030
        }
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<Account> page = null;
        List<Account> list = null;
        page=accountRepo.findAll(pageable);
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespAccountDTO(list),
                page,
                null,null
        );
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Account> optionalAccount = accountRepo.findById(id);
        if(!optionalAccount.isPresent()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }
        Account account = optionalAccount.get();

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(account, RespAccountDTO.class));
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Account> page = null;
        List<Account> list = null;
        switch (columnName){
            case "email":page=accountRepo.findByEmailContainingIgnoreCase(pageable,value);break;
            case "username":page=accountRepo.findByUsernameContainingIgnoreCase(pageable,value);break;
            case "fullname":page=accountRepo.findByFullNameContainingIgnoreCase(pageable,value);break;
            default:page=accountRepo.findAll(pageable);
        }
        list = page.getContent();
        if (list.isEmpty()){
            return GlobalFunction.dataTidakDitemukan(request);
        }
        return transformPagination.transformObject(
                new HashMap<>(),
                convertToListRespAccountDTO(list),
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
        List<Account> accountList = null;
        switch (column){
            case "email":accountList=accountRepo.findByEmailContainingIgnoreCase(value);break;
            case "username":accountList=accountRepo.findByUsernameContainingIgnoreCase(value);break;
            case "fullname":accountList=accountRepo.findByFullNameContainingIgnoreCase(value);break;
            default:accountList=accountRepo.findAll();
        }
        List<RespAccountDTO> listRespAccount = convertToListRespAccountDTO(accountList);
        if (listRespAccount.isEmpty()) {
//            GlobalFunction.manualResponse(response,GlobalFunction.dataTidakDitemukan(request));
            return;
        }
        sBuild.setLength(0);
        String headerKey = "Content-Disposition";
        sBuild.setLength(0);

        String headerValue = sBuild.append("attachment; filename=account_").
                append( new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss.SSS").format(new Date())).//audit trails lewat nama file nya
                        append(".xlsx").toString();//buat excel
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");

        Map<String,Object> map = GlobalFunction.convertClassToObject(new RespAccountDTO());
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
        String[][] strBody = new String[listRespAccount.size()][intListTampungSebentar];
        for (int i = 0; i < listRespAccount.size(); i++) {
            map = GlobalFunction.convertClassToObject(listRespAccount.get(i));
            for (int j = 0; j < intListTampungSebentar; j++) {
                strBody[i][j] = String.valueOf(map.get(loopDataArr[j]));
            }
        }
        new ExcelWriter(strBody, headerArr,"sheet-1", response);
    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List<Account> accountList = null;
//        Map<String,Object> payloadJwt = GlobalFunction.claimsTokenBody(request);
        switch (column) {
            case "username": accountList = accountRepo.findByUsernameContainingIgnoreCase(value);break;

            default:
                accountList = accountRepo.findAll();
        }
        List<RespAccountDTO> listRespMenu = convertToListRespAccountDTO(accountList);
        if (listRespMenu.isEmpty()) {
//            GlobalFunction.manualResponse(response,GlobalFunction.dataTidakDitemukan(request));
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String strHtml = null;
        Context context = new Context();
        Map<String, Object> mapColumnName = GlobalFunction.convertClassToObject(new RespAccountDTO());
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
        map.put("title","REPORT Account");
        context.setVariables(map);
        strHtml = springTemplateEngine.process("global-report",context);
        pdfGenerator.htmlToPdf(strHtml,"Accounts",response);
    }

    public List<RespAccountDTO> convertToListRespAccountDTO(List<Account> accounts){
        return modelMapper.map(accounts,new TypeToken<List<RespAccountDTO>>(){}.getType());
    }

    public Account convertToEntiy(ValAccountDTO valAccountDTO){
        return modelMapper.map(valAccountDTO, Account.class);
    }

}
