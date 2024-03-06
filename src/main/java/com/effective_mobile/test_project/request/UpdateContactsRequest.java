package com.effective_mobile.test_project.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpdateContactsRequest {
    private String username;
    private String newEmail;
    private String newPhone;
}
