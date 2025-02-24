package com.example.webtest.service;

import com.example.webtest.model.HttpRequest;
import com.example.webtest.repository.HttpRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetectionService {

    private final HttpRequestRepository requestRepository;

    // قائمة الأنماط المستخدمة للكشف عن هجمات SQL Injection و XSS
    private static final List<String> SQL_PATTERNS = List.of("select", "union", "insert", "drop");
    private static final List<String> XSS_PATTERNS = List.of("<script>", "alert(", "onerror");

    public DetectionService(HttpRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    // تحليل الطلبات للكشف عن الهجمات
    public boolean analyze(HttpRequest request) {
        String attackType = detectAttack(request);
        if (attackType != null) {
            request.setAttackType(attackType);
            requestRepository.save(request);
            return true;
        }
        // تسجيل الطلبات السليمة
        request.setAttackType("Safe");
        requestRepository.save(request);
        return false;
    }

    // استرجاع جميع السجلات
    public List<HttpRequest> getAllLogs() {
        return requestRepository.findAll();
    }

    // استرجاع السجلات باستخدام الترقيم
    public Page<HttpRequest> getLogs(Pageable pageable) {
        return requestRepository.findAll(pageable);
    }

    // الكشف عن نوع الهجوم
    private String detectAttack(HttpRequest request) {
        String url = request.getUrl().toLowerCase();

        // الكشف عن هجمات SQL Injection
        for (String pattern : SQL_PATTERNS) {
            if (url.contains(pattern)) {
                return "SQL Injection";
            }
        }

        // الكشف عن هجمات XSS
        for (String pattern : XSS_PATTERNS) {
            if (url.contains(pattern)) {
                return "XSS Attack";
            }
        }

        return null;
    }
}
