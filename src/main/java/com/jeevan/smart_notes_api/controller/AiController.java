package com.jeevan.smart_notes_api.controller;

import com.jeevan.smart_notes_api.service.AiService;
import com.jeevan.smart_notes_api.util.FileExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService service;

    @PostMapping("/summarize")
    public String summarize(
            @RequestBody String text) {

        return service.summarize(text);
    }

    @PostMapping("/title")
    public String generateTitle(
            @RequestBody String text) {

        return service.generateTitle(text);
    }

    @PostMapping(
            value = "/file-summary",
            consumes = "multipart/form-data"
    )
    public String summarizeFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("prompt") String prompt) {

        try {

            String text =
                    FileExtractor.extractText(file);

            return service.summarizeWithPrompt(
                    text,
                    prompt
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "File summarization failed"
            );
        }
    }

    @PostMapping(
            value = "/file-title",
            consumes = "multipart/form-data"
    )
    public String generateFileTitle(
            @RequestParam("file") MultipartFile file) {

        try {

            String text =
                    FileExtractor.extractText(file);

            return service.generateTitle(text);

        } catch (Exception e) {

            throw new RuntimeException(
                    "File title generation failed"
            );
        }
    }
}