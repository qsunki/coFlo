package com.reviewping.coflo.treesitter.strategy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ChunkStrategyFactory {

    private final Map<String, ChunkStrategy> strategies = new HashMap<>();

    public ChunkStrategyFactory() {
        strategies.put("base", new BaseChunkStrategy());
        strategies.put("java", new JavaChunkStrategy());
        strategies.put("js", new JavaScriptChunkStrategy());
        strategies.put("ts", new TypeScriptChunkStrategy());
        strategies.put("tsx", new TypeScriptChunkStrategy());
    }

    public ChunkStrategy getStrategy(String extension) {
        return strategies.getOrDefault(extension, strategies.get("base"));
    }
}
