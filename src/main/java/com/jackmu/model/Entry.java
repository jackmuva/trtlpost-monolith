package com.jackmu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Entry")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "entry_id")
    private Long entryId;

    @Column(name = "series_id")
    private Long seriesId;

    //TODO: Look into using LOB for a blob or clob
    //https://www.baeldung.com/jpa-annotation-postgresql-text-type
    @Column(name = "entry_json", columnDefinition="TEXT")
    private String entryJson;

    @Column(name = "entry_html", columnDefinition="TEXT")
    private String entryHtml;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "title")
    private String title;

    @Column(name = "email")
    private String email;
}
