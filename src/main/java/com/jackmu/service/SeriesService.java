package com.jackmu.service;

import com.jackmu.model.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeriesService {
    void saveSeries(Series series);
    void deleteSeries(Long id);
    void incrementReadersForSeries(Long id);
    Page<Series> fetchNewest(Pageable pageable);

    Page<Series> fetchByWriter(Pageable pageable, String writer);
    Page<Series> fetchPublishedByWriter(Pageable pageable, String writer);

    Page<Series> fetchByKeyword(Pageable pageable, String keyword, Boolean published);

    Series fetchBySeriesId(Long id);

    Page<Series> fetchByTag(Pageable pageable, String tag);

    Page<Series> fetchMostPopularAllTime(Pageable pageable);

    Page<Series> fetchMostPopularCurrent(Pageable pageable);

    Page<Series> fetchRandom(Pageable pageable);

    Page<Series> fetchByShelf(Pageable pageable, String shelf);
}
