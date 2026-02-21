package com.hallsymphony.repo;

import com.hallsymphony.model.Issue;
import com.hallsymphony.util.TextFileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IssueRepository {
    private final TextFileStore store = new TextFileStore("data/issues.txt");

    public List<Issue> findAll() {
        List<Issue> issues = new ArrayList<>();
        for (String row : store.readAll()) if (!row.isBlank()) issues.add(Issue.fromRecord(row));
        return issues;
    }

    public Issue save(Issue issue) {
        List<Issue> issues = findAll();
        issues.removeIf(i -> i.getId().equals(issue.getId()));
        issues.add(issue);
        store.writeAll(issues.stream().map(Issue::toRecord).toList());
        return issue;
    }

    public String nextId() { return "I-" + UUID.randomUUID().toString().substring(0, 8); }
}
