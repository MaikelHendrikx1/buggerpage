package com.buggerpage.buggerpage;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

    public Integer addBuggerPage(BuggerPage bp) {
        if (!buggerPageRepository.findByName(bp.name).isPresent()){
            buggerPageRepository.save(bp);
            return bp.id;
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

    public String generateKey(Integer pageId, String ownerPassword, String ownerEmail) {
        BuggerPage bp = buggerPageRepository.findById(pageId).orElseThrow();

        //create a client connected to the Account microservice
        WebClient client = WebClient.create("http://localhost:6003");

        HttpStatus statusCode = client.get()
            .uri("/Account/login?email={em}&password={pw}", ownerEmail, ownerPassword)
            .retrieve()
            .toBodilessEntity()
            .block()
            .getStatusCode();

        if (statusCode == HttpStatus.OK){
            String newKey = bp.generateKey();
            buggerPageRepository.save(bp);
            return newKey;
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Entered the wrong login information.");
        }
    }

    public Boolean verifyKey(Integer pageId, String key) {
        BuggerPage bp = buggerPageRepository.findById(pageId).orElseThrow();
        return bp.KeyCorrect(key);
    }
}
