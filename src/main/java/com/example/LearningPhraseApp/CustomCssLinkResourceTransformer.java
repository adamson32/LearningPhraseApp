package com.example.LearningPhraseApp;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import java.io.IOException;

public class CustomCssLinkResourceTransformer implements ResourceTransformer {


    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        String path = request.getServletPath();
        if (path != null && path.startsWith("/admin/css/")) {
            return resource;
        } else {
            return transformerChain.transform(request, resource);
        }
    }
}
