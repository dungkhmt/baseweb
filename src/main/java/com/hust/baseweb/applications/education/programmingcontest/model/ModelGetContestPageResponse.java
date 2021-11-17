package com.hust.baseweb.applications.education.programmingcontest.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ModelGetContestPageResponse {
    List<ModelGetContestResponse> contents;
}
