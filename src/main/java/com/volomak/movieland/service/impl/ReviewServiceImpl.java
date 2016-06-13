package com.volomak.movieland.service.impl;

import com.volomak.movieland.dao.ReviewDao;
import com.volomak.movieland.entity.Review;
import com.volomak.movieland.service.ReviewService;
import com.volomak.movieland.service.dto.ReviewListDto;
import com.volomak.movieland.service.dto.ReviewListDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private ReviewListDtoConverter reviewListDtoConverter;

    @Override
    public Review getById(int id) {
        return reviewDao.getById(id);
    }

    @Override
    public List<ReviewListDto> getByMovieId(int id) {
        List<ReviewListDto> reviewListDtos = new ArrayList<>();
        for (Review review : reviewDao.getByMovieId(id)){
            reviewListDtos.add(reviewListDtoConverter.converter(review));
        }
        return reviewListDtos;
    }

    @Override
    public void add(Review review) {
        reviewDao.add(review);
    }
}