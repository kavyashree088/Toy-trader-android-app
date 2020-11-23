package com.example.toytrader.admin;

public class IssueModel {
    private String toyName, reason, issueId;

    public IssueModel(String toyName, String reason, String issueId) {
        this.toyName = toyName;
        this.reason = reason;
        this.issueId = issueId;
    }

    public String getToyName() {
        return toyName;
    }

    public void setToyName(String toyName) {
        this.toyName = toyName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
}
