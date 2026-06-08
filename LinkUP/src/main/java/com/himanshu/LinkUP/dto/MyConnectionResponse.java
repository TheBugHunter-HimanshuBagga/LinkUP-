package com.himanshu.LinkUP.dto;

import jakarta.persistence.NamedStoredProcedureQueries;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyConnectionResponse {
    private Long userId;
    private String fullName;
}
