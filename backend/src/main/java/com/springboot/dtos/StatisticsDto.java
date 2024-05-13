package com.springboot.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {
    private long applicationNum;
    private long complexApp;
    private long simpleApp = 0;
    private long incompleteApp = 0;
    private long amlInprogressApp = 0;
}
