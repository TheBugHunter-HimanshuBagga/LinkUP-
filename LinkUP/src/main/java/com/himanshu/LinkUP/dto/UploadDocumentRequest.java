package com.himanshu.LinkUP.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadDocumentRequest {
    private String title;
}

/*
This Multipart file that is used to get the file will be provided inside the controller as
@RequestParam MultipartFile file
 */
