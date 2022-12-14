package com.example.vms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.vms.model.PurchaseList;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseList, Long>{

	PurchaseList save(Optional<PurchaseList> employee);}
