package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation createCompensation(String id, String salary, String effectiveDate){
        Compensation compensation = new Compensation(id, salary, effectiveDate);
        return compensationRepository.save(compensation);
    }

    @Override
    public Compensation getCompensation(String id){
        LOG.debug("Getting compensation with id {}", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Compensation with id " + id + " not found");
        }

        return compensation;
    }

}
