package com.library.repository;

import com.library.model.LibraryBranch;
import java.util.*;

public class BranchRepository {
    private Map<String, LibraryBranch> branches;

    public BranchRepository() {
        this.branches = new HashMap<>();
    }

    public void save(LibraryBranch branch) {
        branches.put(branch.getBranchId(), branch);
    }

    public LibraryBranch findById(String branchId) {
        return branches.get(branchId);
    }

    public List<LibraryBranch> findAll() {
        return new ArrayList<>(branches.values());
    }

    public void delete(String branchId) {
        branches.remove(branchId);
    }
}