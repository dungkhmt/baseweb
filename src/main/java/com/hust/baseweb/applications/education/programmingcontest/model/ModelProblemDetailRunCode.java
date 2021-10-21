package com.hust.baseweb.applications.education.programmingcontest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelProblemDetailRunCode {
    String sourceCode;
    String input;
    String computerLanguage;
}
