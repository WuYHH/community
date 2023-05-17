package com.dlut.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/5/8 15:10
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACED_WORDS = "***";

    //根节点
    private Trie root = new Trie();

    /**
     * 初始化前缀树
     */
    @PostConstruct
    public void init() {

        try (InputStream ins = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(ins));) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("文件读取失败", e.getMessage());
        }
    }

    /**
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // 指针1
        Trie p = root;
        // 指针2
        int start = 0;
        // 指针3
        int end = start;

        StringBuilder sb = new StringBuilder();

        while (start < text.length()) {
            char c = text.charAt(end);
            // 跳过符号
            if (isSymbol(c)) {
                // 如果指针1处于根节点
                if (p == root) {
                    sb.append(c);
                    start++;
                }
                end++;
                continue;
            }
            // 检查下级节点是否为指针2指向的
            p = p.getSubNode(c);
            if (p == null) {
                // 不是敏感词
                sb.append(text.charAt(start));
                // 进入下一个位置
                start++;
                end = start;
                p = root;
            } else if (p.isLeaf()) {
                sb.append(REPLACED_WORDS);
                end++;
                start = end;
                p = root;
            } else {
                // 检查下一个字符
//                end++;
                if (end < text.length() - 1) {
                    end++;
                }
            }
        }
        return sb.toString();
    }

    public boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 根据敏感词构建Trie树
     * @param keyword
     */
    private void addKeyword(String keyword) {
        Trie p = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            Trie subNode = p.getSubNode(c);
            if (subNode == null) {
                subNode = new Trie();
                p.setSubNode(c, subNode);
            }
            p = subNode;
            if (i == keyword.length() - 1) {
                p.setLeaf(true);
            }
        }
    }

    /**
     * 前缀树
     */
    private class Trie{

        private boolean isKeywordEnd = false;

        // 是否为叶子节点
        private boolean isLeaf(){
            return isKeywordEnd;
        }

        private void setLeaf(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 存放子节点
        private Map<Character, Trie> subNode = new HashMap<>();

        // 添加子节点
        public void setSubNode(Character word, Trie trie) {
            subNode.put(word, trie);
        }

        // 获取子节点
        public Trie getSubNode(Character word) {
            return subNode.get(word);
        }

    }

}
