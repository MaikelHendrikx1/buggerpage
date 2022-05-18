package com.buggerpage.buggerpage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
// Simple custom queries can be added with: 'https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation'
// Advanced custom queries can be added with : 'https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query'

public interface BuggerPageRepository extends CrudRepository<BuggerPage, Integer> {

    @Query(nativeQuery = true, value = "SELECT TOP (?1) * FROM bugger_page")
    public Iterable<BuggerPage> getAll(Integer amount);

    @Query(nativeQuery = true, value = "SELECT TOP (?1) * FROM bugger_page WHERE name LIKE %?2%")
    public Iterable<BuggerPage> getAll(Integer amount, String filter);

    public List<BuggerPage> findByOwnerId(Integer ownerId);

    @Query(nativeQuery = true, value = "SELECT id, description, name, owner_id FROM bugger_page INNER JOIN bugger_page_maintainers  ON bugger_page_id = id WHERE maintainers=?1")
    public List<BuggerPage> findByMaintainerId(Integer userId);

    public Optional<BuggerPage> findByName(String name);
}
