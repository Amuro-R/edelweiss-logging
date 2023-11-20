package org.edelweiss.logging.log.el.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.edelweiss.logging.log.el.ILogParseFunction;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultipartParseFunction implements ILogParseFunction {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String functionName() {
        return "MultipartParseFunction";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object parse(Object[] args) {
        if (args != null && args.length > 0) {
            if (args[0] != null) {
                List<MultipartFile> files = new ArrayList<>();
                if (args[0] instanceof List) {
                    files = (List<MultipartFile>) args[0];
                } else if (args[0] instanceof MultipartFile) {
                    files.add((MultipartFile) args[0]);
                }
                return files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.joining(","));
            }
        }
        return null;
    }
}
