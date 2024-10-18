package com.jackmu.model.editorjs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlLink {
    private int success;
    private Map<String, String> meta;
}
