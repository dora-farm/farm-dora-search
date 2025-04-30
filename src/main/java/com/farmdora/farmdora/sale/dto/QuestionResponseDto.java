package com.farmdora.farmdora.sale.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private Integer id;
    private String title;
    private String writer;
    private String content;
    private String answer;
    private boolean isProcess;
    private LocalDateTime createdDate;
}
