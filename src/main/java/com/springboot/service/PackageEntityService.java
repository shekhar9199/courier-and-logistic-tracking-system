package com.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.PackageEntity;
import com.springboot.entity.PackageType;
import com.springboot.repository.PackageEntityRepository;

import jakarta.transaction.Transactional;

@Service
public class PackageEntityService {
	
	@Autowired
	private PackageEntityRepository packageEntityRepository;
	
	@Transactional
	public List<PackageEntity> saveAllPackages(List<PackageEntity> packages) {

	    return packageEntityRepository.saveAll(packages);
	}
	public List<PackageEntity> getByPackageType(PackageType packageType) {

	    return packageEntityRepository.findByPackageType(packageType);
	}
	public List<PackageEntity> getAllPackages() {

	    return packageEntityRepository.findAll();
	}
}
