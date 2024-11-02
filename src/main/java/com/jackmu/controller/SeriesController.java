package com.jackmu.controller;

import com.jackmu.model.Series;
import com.jackmu.service.SeriesService;
import com.jackmu.util.GenericHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesController.class);

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/new")
    public ResponseEntity postSeries(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Series series){
        if(series.getEmail().equals(userDetails.getUsername())){
            series.setNumAllTimeReaders(0);
            series.setNumCurrentReaders(0);
            series.setMaxCurrentReaders(10);
            seriesService.saveSeries(series);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public GenericHttpResponse putSeries(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Series series){
        Series originalSeries = seriesService.fetchBySeriesId(series.getSeriesId());
        if(originalSeries.getEmail().equals(userDetails.getUsername())){
            if(series.getNumAllTimeReaders() != originalSeries.getNumAllTimeReaders() ||
                    series.getNumCurrentReaders() != originalSeries.getNumCurrentReaders() ||
                    series.getMaxCurrentReaders() != originalSeries.getMaxCurrentReaders()){
                return new GenericHttpResponse(HttpStatus.UNAUTHORIZED.value(), "Cannot update reader counts");
            } else {
                seriesService.saveSeries(series);
                return new GenericHttpResponse(HttpStatus.OK.value(), "Series successfully updated");
            }
        } else {
            return new GenericHttpResponse(HttpStatus.BAD_REQUEST.value(), "User Unauthorized");
        }
    }

    @DeleteMapping("/delete/{seriesId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteSeries(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long seriesId){
        if(seriesService.fetchBySeriesId(seriesId).getEmail().equals(userDetails.getUsername())){
            seriesService.deleteSeries(seriesId);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getNewest")
    public List<Series> getNewestSeries(@RequestParam(defaultValue = "0") int page){
        try{
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchNewest(paging);
            return pageSeries.getContent();
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("/writer/{writer}")
    public List<Series> getSeriesByWriter(@RequestParam(defaultValue = "0") int page,
                                                   @PathVariable String writer){
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchByWriter(paging, writer);
            return pageSeries.getContent();
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("/writer/published/{writer}")
    public List<Series> getPublishedSeriesByWriter(@RequestParam(defaultValue = "0") int page,
                                          @PathVariable String writer){
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchPublishedByWriter(paging, writer);
            return pageSeries.getContent();
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("/search/{keyword}")
    public List<Series> searchPublishedSeries(@PathVariable String keyword, @RequestParam(defaultValue = "0") int page){
        try{
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchByKeyword(paging, keyword, Boolean.TRUE);
            return pageSeries.getContent();
        } catch (Exception e){
            return null;
        }
    }

    @GetMapping("/{id}")
    public Series getSeriesById(@PathVariable Long id){
        return seriesService.fetchBySeriesId(id);
    }

    @GetMapping("/tag/{tag}")
    public Object getSeriesByTag(@RequestParam(defaultValue = "0") int page,
                                       @PathVariable String tag){
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchByTag(paging, tag);
            return pageSeries.getContent();
        } catch (Exception e){
            return new GenericHttpResponse(HttpStatus.BAD_REQUEST.value(), "unable to fetch by tag");
        }
    }

    @GetMapping("/popularAllTime")
    public Object getPopularAllTime(@RequestParam(defaultValue = "0") int page){
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchMostPopularAllTime(paging);
            return pageSeries.getContent();
        } catch (Exception e){
            return new GenericHttpResponse(HttpStatus.BAD_REQUEST.value(), "unable to fetch most popular all time series");
        }
    }

    @GetMapping("/popularCurrent")
    public Object getPopularCurrent(@RequestParam(defaultValue = "0") int page){
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchMostPopularCurrent(paging);
            return pageSeries.getContent();
        } catch (Exception e){
            return new GenericHttpResponse(HttpStatus.BAD_REQUEST.value(), "unable to fetch most popular current series");
        }
    }

    @GetMapping("/random")
    public Object getRandom(@RequestParam(defaultValue = "0") int page){
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchRandom(paging);
            return pageSeries.getContent();
        } catch (Exception e){
            return new GenericHttpResponse(HttpStatus.BAD_REQUEST.value(), "unable to fetch random series");
        }
    }

    @GetMapping("/shelf/{shelf}")
    public Object getByShelf(@RequestParam(defaultValue = "0") int page, @PathVariable String shelf){
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Series> pageSeries = seriesService.fetchByShelf(paging, shelf);
            return pageSeries.getContent();
        } catch (Exception e){
            return new GenericHttpResponse(HttpStatus.BAD_REQUEST.value(), "unable to fetch by shelf");
        }
    }
}
