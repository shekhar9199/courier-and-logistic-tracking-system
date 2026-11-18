package com.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.PackageEntity;
import com.springboot.entity.PackageType;

public interface PackageEntityRepository
        extends JpaRepository<PackageEntity, Integer> {

    List<PackageEntity> findByPackageType(PackageType packageType);
}