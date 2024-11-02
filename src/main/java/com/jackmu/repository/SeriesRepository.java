package com.jackmu.repository;

import com.jackmu.model.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    Page<Series> findByPublishedIsTrueOrderByDatetimeDesc(Pageable pageable);
    Page<Series> findByPenNameIgnoreCaseAndPublishedIsTrueOrderByDatetimeDesc(Pageable pageable, String penName);
    Page<Series> findByPenNameIgnoreCaseOrderByDatetimeDesc(Pageable pageable, String penName);
    List<Series> findAllByTagsIsContainingIgnoreCaseAndPublishedIsTrue(String tag);
    @Query(value = "SELECT * FROM Series WHERE (POSITION(LOWER(:penName) in LOWER(pen_name)) > 0 " +
            "OR POSITION(LOWER(:tag) in LOWER(tags)) > 0 OR POSITION(LOWER(:summary) in LOWER(summary)) > 0 " +
            "OR POSITION(LOWER(:title) in LOWER(title)) > 0) AND published = :published",
    nativeQuery = true)
    Page<Series> findAllByKeyword(
            Pageable pageable,
            @Param("penName") String penName,
            @Param("tag") String tag,
            @Param("summary") String summary,
            @Param("title") String title,
            @Param("published") Boolean published
    );
    Series findBySeriesId(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE series " +
            "SET num_all_time_readers = num_all_time_readers + 1, num_current_readers = num_current_readers + 1 " +
            "WHERE series_id = :seriesId",
    nativeQuery = true)
    void incrementReaders(Long seriesId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Series SET num_current_readers = num_current_readers - :numReaders " +
            "WHERE series_id = :seriesId",
            nativeQuery = true)
    void decrementCurrentReaders(@Param("seriesId") Long seriesId,
                                 @Param("numReaders") Integer numReaders);

    Page<Series> findAllByTagsIsContainingIgnoreCaseAndPublishedIsTrueOrderByDatetimeDesc(Pageable pageable, String tag);

    Page<Series> findAllByShelfIsContainingIgnoreCaseAndPublishedIsTrueOrderByDatetimeDesc(Pageable pageable, String shelf);

    Page<Series> findAllByPublishedIsTrueOrderByNumAllTimeReadersDesc(Pageable pageable);

    Page<Series> findAllByPublishedIsTrueOrderByNumCurrentReadersDesc(Pageable pageable);

    @Query(nativeQuery=true, value="SELECT *  FROM Series ORDER BY RANDOM()")
    Page<Series> getRandomSeries(Pageable pageable);
}
