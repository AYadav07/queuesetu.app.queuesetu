package com.queuesetu.queuesetu.controller;

import com.queuesetu.account.dto.Branch;
import com.queuesetu.account.dto.BranchRequest;
import com.queuesetu.queuesetu.service.AccountClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@Tag(name = "Branch API", description = "Branch management proxied to account service")
public class BranchController {

    @Autowired
    private AccountClientService accountClientService;

    @PostMapping
    @Operation(summary = "Create a new branch")
    public ResponseEntity<Branch> createBranch(@Valid @RequestBody BranchRequest body,
                                               HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.createBranch(body, authHeader));
    }

    @GetMapping("/{branchId}")
    @Operation(summary = "Get branch by ID")
    public ResponseEntity<Branch> getBranch(@PathVariable String branchId,
                                            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.getBranch(branchId, authHeader));
    }

    @PutMapping("/{branchId}")
    @Operation(summary = "Update branch by ID")
    public ResponseEntity<Branch> updateBranch(@PathVariable String branchId,
                                               @Valid @RequestBody BranchRequest body,
                                               HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.updateBranch(branchId, body, authHeader));
    }

    @DeleteMapping("/{branchId}")
    @Operation(summary = "Delete branch by ID")
    public ResponseEntity<String> deleteBranch(@PathVariable String branchId,
                                               HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.deleteBranch(branchId, authHeader));
    }
}
