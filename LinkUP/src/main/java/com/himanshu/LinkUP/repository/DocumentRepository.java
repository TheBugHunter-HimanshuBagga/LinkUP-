package com.himanshu.LinkUP.repository;

import com.himanshu.LinkUP.entity.Document;
import com.himanshu.LinkUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByUploadedBy(User uploadedBy);
}
