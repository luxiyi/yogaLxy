package com.woniu.yago.service.impl;

import com.woniu.yago.pojo.Venue;
import com.woniu.yago.repository.VenueRepository;
import com.woniu.yago.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @Description: java类作用描述VenueServiceImpl
 * @Author: lxy
 * @time: 2019/4/25 17:40
 */
@Service
@Repository
public class VenueServiceImpl implements VenueService {
//    @Autowired
//    private VenueMapper venueMapper;
    @Autowired
    private VenueRepository venueRepository;

    @Override
    public void saveVenue(Venue venue) {
        venueRepository.save(venue);
    }
}
