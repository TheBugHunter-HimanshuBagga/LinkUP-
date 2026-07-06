package com.himanshu.LinkUP.service.impl;

import com.himanshu.LinkUP.dto.DocumentResponse;
import com.himanshu.LinkUP.entity.Document;
import com.himanshu.LinkUP.entity.User;
import com.himanshu.LinkUP.enums.ActivityType;
import com.himanshu.LinkUP.repository.DocumentRepository;
import com.himanshu.LinkUP.repository.UserRepository;
import com.himanshu.LinkUP.service.ActivityService;
import com.himanshu.LinkUP.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    @Override
    public String uploadDocument(MultipartFile file, String title){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("Current User not found")
                );
        if(file.isEmpty()){
            throw new RuntimeException("ERROR!!!  No file selected");
        }

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        Path uploadPath = Paths.get("uploads/documents");

        try {

            // Create folder if it doesn't exist
            Files.createDirectories(uploadPath);

            // Copy file from request to uploads/profile
            Files.copy(
                    file.getInputStream(), // source
                    uploadPath.resolve(fileName) // destination
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload document", e);
        }

        Document document = Document.builder()
                .title(title)
                .fileUrl(
                        "uploads/documents/" + fileName
                )
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(currentUser)
                .build();

        documentRepository.save(document);

        activityService.createActivity(
                currentUser,
                "Uploaded a new document.",
                ActivityType.DOCUMENT_UPLOADED
        );

        return "Document Uploaded Successfully";
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("Current user not found")
                );
        return documentRepository.findByUploadedBy(currentUser)
                .stream()
                .map(document -> DocumentResponse.builder()
                        .id(document.getId())
                        .title(document.getTitle())
                        .fileUrl(document.getFileUrl())
                        .uploadedAt(document.getUploadedAt())
                        .build())
                .toList();
    }

    @Override
    public String deleteDocument(Long documentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Current User Not Found")
        );
        // document should also exists
        Document document = documentRepository.findById(documentId).orElseThrow(
                () -> new RuntimeException("Document not found")
        );

        if(!document.getUploadedBy().getId().equals(currentUser.getId())){
            throw new RuntimeException(
                    "You are not authorized to delete this document"
            );
        }

        documentRepository.delete(document);
        return "Document Deleted Successfully";
    }
}
