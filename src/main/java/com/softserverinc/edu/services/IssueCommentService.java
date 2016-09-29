package com.softserverinc.edu.services;

import com.softserverinc.edu.entities.Issue;
import com.softserverinc.edu.entities.IssueComment;
import com.softserverinc.edu.repositories.IssueCommentRepository;
import com.softserverinc.edu.services.securityServices.IssueCommentSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IssueCommentService {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssueCommentSecurityService issueCommentSecurityService;

    @Autowired
    private IssueCommentRepository issueCommentRepository;

    public IssueComment findOne(Long id) {
        return issueCommentRepository.findOne(id);
    }

    public List<IssueComment> findByIssue(Issue issue) {
        return issueCommentRepository.findByIssue(issue);
    }

    public List<IssueComment> findAll() {
        return issueCommentRepository.findAll();
    }

    @Transactional
    public IssueComment save(IssueComment issueComment) {
        return issueCommentRepository.saveAndFlush(issueComment);
    }

    @Transactional
    public void delete(Long id) {
        issueCommentRepository.delete(id);
    }

    @Transactional
    public IssueComment update(IssueComment issueComment) {
        return issueCommentRepository.saveAndFlush(issueComment);
    }

    public List<IssueComment> findByIssueId(Long issueId) {
        return issueCommentRepository.findByIssue(issueService.findById(issueId));
    }

    public boolean isCommentNew(IssueComment comment) {
        return (comment.getId() == null || comment.getId() == 0L);
    }

    public IssueComment getEditedCommentById(Long issueCommentId) {
        IssueComment issueComment = findOne(issueCommentId);
        issueComment.setIsEdited(true);
        return issueComment;
    }

    public IssueComment getNewIssueComment(Long issueId) {
        IssueComment issueComment = new IssueComment();
        issueComment.setIssue(issueService.findById(issueId));
        issueComment.setIsEdited(false);
        if (issueCommentSecurityService.isAuthenticated()){
            issueComment.setUser(userService.findOne(issueCommentSecurityService.getActiveUser().getId()));
        }
        return issueComment;
    }

}