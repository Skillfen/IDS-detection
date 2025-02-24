package com.example.webtest.repository;

import com.example.webtest.model.HttpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpRequestRepository extends JpaRepository<HttpRequest, Long> {

}
