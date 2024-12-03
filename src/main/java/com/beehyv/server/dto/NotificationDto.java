package com.beehyv.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long subjectId;
    private Boolean readStatus;
    private String title;
    private String description;
}
