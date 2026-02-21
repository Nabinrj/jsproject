package com.hallsymphony.service;

import com.hallsymphony.model.Hall;
import com.hallsymphony.model.HallType;
import com.hallsymphony.repo.HallRepository;

import java.util.List;

public class HallService {
    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public List<Hall> findAll() { return hallRepository.findAll(); }

    public Hall create(String name, HallType type, String remark) {
        Hall hall = new Hall(hallRepository.nextId(), name, type, remark);
        return hallRepository.save(hall);
    }

    public Hall update(Hall hall, String name, HallType type, String remark) {
        hall.setName(name);
        hall.setType(type);
        hall.setAvailabilityRemark(remark);
        return hallRepository.save(hall);
    }

    public void delete(String hallId) { hallRepository.delete(hallId); }
}
