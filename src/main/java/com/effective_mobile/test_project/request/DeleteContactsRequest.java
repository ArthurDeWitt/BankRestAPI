package com.effective_mobile.test_project.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DeleteContactsRequest {
    private String username;
    private String contactType;
}
