package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.DocumentResponse;
import com.himanshu.LinkUP.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    String uploadDocument(MultipartFile file, String title);

    List<DocumentResponse> getAllDocuments();

    String deleteDocument(Long documentId);
}
