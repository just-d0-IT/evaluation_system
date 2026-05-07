package com.zhixinsiwei.evaluation_system.utils;

import lombok.Data;

import java.util.List;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName QuestionDetail.java
 * @Description TODO
 * @createTime 2026年01月09日 22:09:00
 */
@Data
public class QuestionDetail {

    private Question question;
    private List<Option> options;

    @Data
    public static class Question {
        private String url;
    }

    @Data
    public static class Option {
        private String option;   // A / B / C ...
        private Details details;

        @Data
        public static class Details {
            private String url;
        }
    }
}
