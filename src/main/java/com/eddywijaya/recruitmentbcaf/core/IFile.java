package com.eddywijaya.recruitmentbcaf.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IFile<G> {
    public String uploadFile(MultipartFile multipartFile, HttpServletRequest request);//061-065
    public ResponseEntity<Object> readOCR(MultipartFile multipartFile, HttpServletRequest request);//066-070
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response);//081-090
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response);//091-100
}