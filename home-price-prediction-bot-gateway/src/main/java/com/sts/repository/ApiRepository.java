package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Api;

public interface ApiRepository extends JpaRepository<Api, String> {

}
