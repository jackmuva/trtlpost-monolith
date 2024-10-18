package com.jackmu.model.editorjs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ImageLookup")
public class ImageLookup {
    @Id
    @Column(name = "image_filename")
    private String imageFilename;

    @Column(name = "entry_id")
    private Long entryId;
}
