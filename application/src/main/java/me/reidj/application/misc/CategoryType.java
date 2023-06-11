package me.reidj.application.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoryType {
    WEBSITE_PROBLEMS("Проблемы с сайтом"),
    TECHNICAL_PROBLEMS("Проблемы с техникой"),
    SOFTWARE_INSTALLATION("Установка ПО"),
    OTHER("Другое"),
    ;

    private final String title;
}
