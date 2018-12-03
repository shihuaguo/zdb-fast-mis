package com.zdb.modules.taxic.entity;

import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;

import javax.validation.constraints.NotNull;

public class QuestionWithBLOBs extends Question {

    @NotNull(message="问题内容不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String contentSummary;

    @NotNull(message="问题回复", groups = {AddGroup.class, UpdateGroup.class})
    private String replySolution;

    public String getContentSummary() {
        return contentSummary;
    }

    public void setContentSummary(String contentSummary) {
        this.contentSummary = contentSummary == null ? null : contentSummary.trim();
    }

    public String getReplySolution() {
        return replySolution;
    }

    public void setReplySolution(String replySolution) {
        this.replySolution = replySolution == null ? null : replySolution.trim();
    }
}