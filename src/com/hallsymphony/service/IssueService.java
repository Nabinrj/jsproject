package com.hallsymphony.service;

import com.hallsymphony.model.Issue;
import com.hallsymphony.model.IssueStatus;
import com.hallsymphony.repo.IssueRepository;

import java.util.List;

public class IssueService {
    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<Issue> findAll() { return issueRepository.findAll(); }

    public Issue raiseIssue(String bookingId, String customerId, String description) {
        Issue issue = new Issue(issueRepository.nextId(), bookingId, customerId, description, IssueStatus.IN_PROGRESS, "");
        return issueRepository.save(issue);
    }

    public void assignAndUpdate(Issue issue, String schedulerId, IssueStatus status) {
        issue.setAssignedSchedulerId(schedulerId);
        issue.setStatus(status);
        issueRepository.save(issue);
    }
}
