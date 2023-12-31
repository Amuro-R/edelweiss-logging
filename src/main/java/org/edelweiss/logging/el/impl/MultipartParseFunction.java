package org.edelweiss.logging.el.impl;


import org.edelweiss.logging.el.ILogParseFunction;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class MultipartParseFunction implements ILogParseFunction {

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
