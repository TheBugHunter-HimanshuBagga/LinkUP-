package com.himanshu.LinkUP.controller;

import com.himanshu.LinkUP.dto.DocumentResponse;
import com.himanshu.LinkUP.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;


    @PostMapping
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
                                                 @RequestParam String title){
        String message = documentService.uploadDocument(file,title);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/me")
    public ResponseEntity<List<DocumentResponse>> getAllDocuments(){
        List<DocumentResponse> documentResponseList = documentService.getAllDocuments();
        return ResponseEntity.ok(documentResponseList);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long documentId){
        String message = documentService.deleteDocument(documentId);
        return ResponseEntity.ok(message);
    }
}
