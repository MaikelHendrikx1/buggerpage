package com.buggerpage.buggerpage;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BuggerPageService {
    @Autowired
    private BuggerPageRepository buggerPageRepository;

    public BuggerPage getBugReportbyId(Integer pageId) {
        return buggerPageRepository.findById(pageId).orElseThrow();
    }

    public Iterable<BuggerPage> getAll(String filter, Integer amount) {
        if (filter == null){
            return buggerPageRepository.getAll(amount);
        }
        else{
            return buggerPageRepository.getAll(amount, filter);
        }
    }

    public String addBuggerPage(BuggerPage bp) {
        if (!buggerPageRepository.findByName(bp.name).isPresent()){
            buggerPageRepository.save(bp);
            return "Succesfully added " + bp.toString();
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A bugger page with this name already exists.");
        }
    }

    public Iterable<BuggerPage> getAllByUser(Integer userID){
        var a = buggerPageRepository.findByOwnerId(userID);
        var b = buggerPageRepository.findByMaintainerId(userID);
        
        a.addAll(b);

        return a;
    }

    public void addMaintainers(Set<Integer> userIds, Integer pageId) {
        var entity = buggerPageRepository.findById(pageId).orElseThrow();
        entity.maintainers.addAll(userIds);
        buggerPageRepository.save(entity);
    }

    public void deleteMaintainers(Set<Integer> userIds, Integer pageId) {
        var entity = buggerPageRepository.findById(pageId).orElseThrow();
        entity.maintainers.removeAll(userIds);
        buggerPageRepository.save(entity);
    }
}
