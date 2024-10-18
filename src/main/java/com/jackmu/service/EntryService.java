package com.jackmu.service;

import com.jackmu.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntryService {
    Entry saveEntry(Entry entry);
    Entry updateEntry(Entry entry);
    void deleteEntry(Long id);
    Page<Entry> fetchEntriesBySeriesId(Pageable pageable, Long id);
    List<Entry> fetchEntriesByEntryId(Long id);

    Entry fetchFirstEntryBySeriesId(Long id);
}
