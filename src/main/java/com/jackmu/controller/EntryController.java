package com.jackmu.controller;

import com.jackmu.model.Entry;
import com.jackmu.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/entry")
public class EntryController {
    @Autowired
    private EntryService entryService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/new")
    public ResponseEntity<Entry> postEntry(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Entry entry){
        if(entry.getEmail().equals(userDetails.getUsername())) {
            return new ResponseEntity<>(entryService.saveEntry(entry), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<Entry> putEntry(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Entry entry){
        if(entryService.fetchEntriesByEntryId(entry.getEntryId()).get(0).getEmail().equals(userDetails.getUsername())){
            return new ResponseEntity<>(entryService.updateEntry(entry), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEntry(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id){
        if(entryService.fetchEntriesByEntryId(id).get(0).getEmail().equals(userDetails.getUsername())){
            entryService.deleteEntry(id);
            return new ResponseEntity(HttpStatus.OK);
}
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getBySeries/{id}")
    public List<Entry> getBySeriesId(@RequestParam(defaultValue = "0") int page,
                                     @PathVariable Long id){
        try {
            Pageable paging = PageRequest.of(page, 10, Sort.by("orderNum").ascending());
            Page<Entry> pageSeries = entryService.fetchEntriesBySeriesId(paging, id);
            return pageSeries.getContent();
        } catch(Exception e){
            return null;
        }
    }

    @GetMapping("/getFirstBySeries/{id}")
    public Entry getFirstBySeriesId(@PathVariable Long id){
        return entryService.fetchFirstEntryBySeriesId(id);
    }

}
