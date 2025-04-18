package com.example.api;

import com.example.api.HistoryDTO;
import com.example.summarizer.SummarizerBridge;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/summarizer")
public class SummarizerController {

    @GetMapping("/summary")
    public String summarize(@RequestParam String url1, @RequestParam String url2) throws ExecutionException, InterruptedException {
        CompletableFuture<String> summaryFuture1 = SummarizerBridge.summarize(url1);
        CompletableFuture<String> summaryFuture2 = SummarizerBridge.summarize(url2);

        // Wait for both summaries and return them
        StringBuilder sb = new StringBuilder();
        sb.append(summaryFuture1.get()).append("\n\n").append(summaryFuture2.get());

        return sb.toString();
    }

    @DeleteMapping("/deleteHistory")
    public int deleteHistory(@RequestParam int id) {
        try {
            SummarizerBridge.deleteHistory(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @GetMapping("/history")
    public List<HistoryDTO> getHistory() {
        return SummarizerBridge.getHistory().stream()
                .map(h -> new HistoryDTO(h.id(), h.url(), h.summary(), h.requestedAt()))
                .collect(Collectors.toList());
    }
}
