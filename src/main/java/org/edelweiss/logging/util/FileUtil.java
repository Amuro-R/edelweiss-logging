package org.edelweiss.logging.util;

import org.edelweiss.logging.exception.LoggingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtil {

    /**
     * 向上路径搜索
     *
     * @param baseFile      基础目录/文件
     * @param targetFile    目标目录/文件
     * @param maxSearchTime 最大向上查找层数
     * @return 目标目录/文件 绝对路径
     */
    public static String pathUpperSearch(File baseFile, String targetFile, int maxSearchTime) {
        log.info("路径搜索入参: 基础目录/文件绝对路径:{} 目标目录/文件名称:{} 最大向上查找层数:{}", baseFile.getAbsolutePath(), targetFile, maxSearchTime);
        String basePath = null;
        String finalPath = null;
        try {
            basePath = baseFile.getCanonicalPath();
        } catch (IOException e) {
            throw new LoggingException("getCanonicalPath 失败", e);
        }
        if (ObjectUtils.isEmpty(basePath)) {
            throw new LoggingException("初始路径获取失败");
        }
        log.info("基础路径: {}", basePath);
        Path path = Paths.get(basePath);
        boolean find = false;
        int time = 0;
        do {
            Path temp = path.resolve(targetFile);
            log.info("查找当前目录下目标文件: 次数:{} 目标文件:{}", time, temp.toUri().getPath());
            boolean exist = Files.exists(temp);
            if (!exist) {
                log.info("当前目录下目标文件不存在，进入父目录查找");
                path = path.getParent();
                time++;
                continue;
            }
            log.info("找到目标文件");
            find = true;
            log.info("当前操作系统 {}", System.getProperty("os.name").toLowerCase());
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                log.info("win系统去除前导斜杠");
                basePath = path.toUri().getPath().substring(1);
            } else {
                basePath = path.toUri().getPath();
            }
            log.info("目标所在目录 {}", basePath);
            finalPath = basePath + targetFile;
            log.info("目标绝对路径 {}", finalPath);
        } while (path != null && !find && time <= maxSearchTime);
        if (ObjectUtils.isEmpty(finalPath)) {
            log.error("未找到对应的文件路径");
            throw new LoggingException("未找到对应的文件路径");
        }
        return finalPath;
    }

    public static File fileUpperSearch(File baseFile, String targetFile, int maxSearchTime) {
        try {
            String finalPath = FileUtil.pathUpperSearch(baseFile, targetFile, maxSearchTime);
            File finalFile = new File(finalPath);
            if (!finalFile.exists()) {
                throw new LoggingException("未找到对应路径下的文件");
            }
            return finalFile;
        } catch (Exception e) {
            log.error("文件查询异常", e);
            return null;
        }
    }

}
