package com.jackmu.service;

import com.jackmu.model.Entry;
import com.jackmu.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EntryServiceImpl implements EntryService{
    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private ImageService imageService;

    public Entry saveEntry(Entry entry){
        return entryRepository.save(entry);
    }

    public Entry updateEntry(Entry entry){
        if(!Objects.equals(entry.getOrderNum(), fetchEntriesByEntryId(entry.getEntryId()).get(0).getOrderNum())){
            changeOtherEntryOrders(entry);
        }

        return entryRepository.save(entry);
    }

    public void deleteEntry(Long id){
        Entry entry = entryRepository.findByEntryId(id).get(0);
        imageService.deleteImagesInEntry(id);
        moveUpOtherEntryOrders(entry);
        entryRepository.deleteByEntryId(id);
    }

    public Page<Entry> fetchEntriesBySeriesId(Pageable pageable, Long id){
        return entryRepository.findAllBySeriesId(pageable, id);
    }
    public List<Entry> fetchEntriesByEntryId(Long id){
        return entryRepository.findByEntryId(id);
    }

    public Entry fetchFirstEntryBySeriesId(Long id){ return entryRepository.findBySeriesIdAndOrderNum(id, 1);}

    protected void changeOtherEntryOrders(Entry entry){
        Integer oldOrderNum = fetchEntriesByEntryId(entry.getEntryId()).get(0).getOrderNum();
        Integer newOrderNum = entry.getOrderNum();
        if(oldOrderNum > newOrderNum){
            pushBackOtherEntryOrders(entry, newOrderNum, oldOrderNum);
        } else {
            moveUpOtherEntryOrders(entry, newOrderNum, oldOrderNum);
        }
    }

    protected void pushBackOtherEntryOrders(Entry entry, Integer newOrderNum, Integer oldOrderNum){
        List<Entry> allEntries = entryRepository.findAllBySeriesId(entry.getSeriesId());
        for(Entry ent:allEntries){
            if(ent.getOrderNum() >= newOrderNum && ent.getOrderNum() <= oldOrderNum){
                ent.setOrderNum(ent.getOrderNum() + 1);
                entryRepository.save(ent);
            }
        }
    }

    protected void moveUpOtherEntryOrders(Entry entry, Integer newOrderNum, Integer oldOrderNum){
        List<Entry> allEntries = entryRepository.findAllBySeriesId(entry.getSeriesId());

        for(Entry ent:allEntries){
            if(ent.getOrderNum() <= newOrderNum && ent.getOrderNum() >= oldOrderNum){
                ent.setOrderNum(ent.getOrderNum() - 1);
                entryRepository.save(ent);
            }
        }
    }

    protected void moveUpOtherEntryOrders(Entry entry){
        List<Entry> allEntries = entryRepository.findAllBySeriesId(entry.getSeriesId());

        for(Entry ent:allEntries){
            if(ent.getOrderNum() > entry.getOrderNum()){
                ent.setOrderNum(ent.getOrderNum() - 1);
                entryRepository.save(ent);
            }
        }
    }
}
