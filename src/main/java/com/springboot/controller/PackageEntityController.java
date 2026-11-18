package com.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entity.PackageEntity;
import com.springboot.entity.PackageType;
import com.springboot.service.PackageEntityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/package")
public class PackageEntityController {
	
	@Autowired
	private PackageEntityService packageEntityService;
	
	@PostMapping("/saveAll")
	public ResponseEntity<List<PackageEntity>> saveAllPackages(
	        @Valid @RequestBody List<@Valid PackageEntity> packages) {

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(packageEntityService.saveAllPackages(packages));
	}
	@GetMapping("/type/{packageType}")
	public ResponseEntity<List<PackageEntity>> getByPackageType(
	        @PathVariable PackageType packageType) {

	    return ResponseEntity.ok(
	            packageEntityService.getByPackageType(packageType)
	    );
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<PackageEntity>> getAllPackages() {

	    return ResponseEntity.ok(
	            packageEntityService.getAllPackages()
	    );
	}
}
