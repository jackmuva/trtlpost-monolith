package com.jackmu.service;

import com.jackmu.model.Entry;
import com.jackmu.model.Series;
import com.jackmu.repository.EntryRepository;
import com.jackmu.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesServiceImpl implements SeriesService{
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private ImageService imageService;

    public void saveSeries(Series series){
        seriesRepository.save(series);
    }

    public void deleteSeries(Long id){
        for(Entry entry : entryRepository.findAllBySeriesId(id)){
            imageService.deleteImagesInEntry(entry.getEntryId());
        }
        entryRepository.deleteBySeriesId(id);
        seriesRepository.deleteById(id);
    }

    @Override
    public void incrementReadersForSeries(Long id) {
        seriesRepository.incrementReaders(id);
    }

    public Page<Series> fetchNewest(Pageable pageable){
        return seriesRepository.findByPublishedIsTrueOrderByDatetimeDesc(pageable);
    }

    public Page<Series> fetchByWriter(Pageable pageable, String penName){
        return seriesRepository.findByPenNameIgnoreCaseOrderByDatetimeDesc(pageable, penName);
    }

    public Page<Series> fetchPublishedByWriter(Pageable pageable, String penName){
        return seriesRepository.findByPenNameIgnoreCaseAndPublishedIsTrueOrderByDatetimeDesc(pageable, penName);
    }

    public Page<Series> fetchByTag(Pageable pageable, String tag){
        return seriesRepository.findAllByTagsIsContainingIgnoreCaseAndPublishedIsTrueOrderByDatetimeDesc(pageable, tag);
    }

    @Override
    public Page<Series> fetchMostPopularAllTime(Pageable pageable) {
        return seriesRepository.findAllByPublishedIsTrueOrderByNumAllTimeReadersDesc(pageable);
    }

    @Override
    public Page<Series> fetchMostPopularCurrent(Pageable pageable) {
        return seriesRepository.findAllByPublishedIsTrueOrderByNumCurrentReadersDesc(pageable);
    }

    @Override
    public Page<Series> fetchRandom(Pageable pageable) {
        return seriesRepository.getRandomSeries(pageable);
    }

    @Override
    public Page<Series> fetchByShelf(Pageable pageable, String shelf) {
        return seriesRepository.findAllByShelfIsContainingIgnoreCaseAndPublishedIsTrueOrderByDatetimeDesc(pageable, shelf);
    }

    public Page<Series> fetchByKeyword(Pageable pageable, String keyword, Boolean published){
        return seriesRepository.findAllByKeyword(pageable, keyword, keyword, keyword, keyword, published);
    }

    public Series fetchBySeriesId(Long id){
        return seriesRepository.findBySeriesId(id);
    }

}
