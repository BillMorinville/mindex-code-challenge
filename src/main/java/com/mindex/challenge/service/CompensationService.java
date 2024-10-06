package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation createCompensation(String id, String salary, String effectiveDate);
    Compensation getCompensation(String id);
}
