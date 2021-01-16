package com.jerryio.publicbin.disk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/*
* From: https://www.spigotmc.org/threads/save-yaml-files-with-comments.409965
*/
public class CommentYamlConfiguration extends YamlConfiguration {

    /*
    * comments.put("path.prior.to.comment", "comment")
    */
    public final Map<String, List<String>> comments;
    public String firstPath;

    public CommentYamlConfiguration() {
        comments = new HashMap<>();
    }

    @Override
    public String saveToString() {
        super.options().header(null);
        
        String contents = super.saveToString();

        List<String> list = new ArrayList<>();
        Collections.addAll(list, contents.split("\n"));
        
        PathHandler ph = new PathHandler();
        
        StringBuilder sb = new StringBuilder();

        for(Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
            String line = iterator.next();

            if(!line.isEmpty() && line.contains(":")) {
                ph.addkeyLine(line);
                String path = ph.getPath();
                if(comments.containsKey(path)) {
                    comments.get(path).forEach(string -> {
                        sb.append(string);
                        sb.append('\n');
                    });
                }
            }
            sb.append(line);
            sb.append('\n');
        }
        
        comments.get(":ENDLINE:").forEach(string -> {
            sb.append(string);
            sb.append('\n');
        });

        return sb.toString();
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        if (contents.equals(""))
            throw new IllegalArgumentException("The config file should not be blank");

        super.loadFromString(contents);
        List<String> list = new ArrayList<>();
        Collections.addAll(list, contents.split("\n"));
        
        PathHandler ph = new PathHandler();
        List<String> cacheComments = new ArrayList<>();

        for(Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
            String line = iterator.next();

            String trimmed = line.trim();
            if(trimmed.startsWith("#") || trimmed.isEmpty()) {
                cacheComments.add(line);
            } else if(line.contains(":")) {
                ph.addkeyLine(line);
                String path = ph.getPath();
                
                if (firstPath == null) firstPath = path;
                
                comments.put(path, cacheComments);
                cacheComments = new ArrayList<>();
            }
        }
        
        comments.put(":ENDLINE:", cacheComments);
    }
    
    public void addDefaultsWithComments(CommentYamlConfiguration defaults) {
        Validate.notNull(defaults, "Defaults may not be null");

        addDefaults(defaults.getValues(true));
        
        for (Map.Entry<String, List<String>> entry : defaults.comments.entrySet())
            if (comments.containsKey(entry.getKey()) == false)
                comments.put(entry.getKey(), entry.getValue());
        
        List<String> firstComment = comments.get(firstPath);
        if (firstComment.size() > 0 && firstComment.get(0).equals("") == false)
            firstComment.add(0, ""); // add new line at the beginning
    }
    
    public static final class PathHandler {
        public Stack<String> currentPath;
        public Stack<Integer> currentLevel;
        public Map<Integer, Integer> keyCounters;
        
        public PathHandler() {
            currentPath = new Stack<>();
            currentLevel = new Stack<>();
            keyCounters = new HashMap<>();
        }
        
        public void addkeyLine(String line) {
            int spaces = getSpaceFromLine(line);
            int layers = getLayerFromLine(currentLevel, spaces);
                            
            while (currentPath.size() != 0 && layers < currentPath.size()) {
                currentPath.pop();
                currentLevel.pop();
            }

            String key = getKeyFromLine(line);
            
            currentPath.push(key);
            currentLevel.push(spaces);
        }
        
        public String getPath() {
            String rtn = "";
            
            for (int index = 0; index < currentPath.size(); index++) {            
                rtn += (index != 0 ? "." : "") + currentPath.get(index);
            }
            return rtn;
        }
        
        private String getKeyFromLine(String line) {
            String key = "";

            for(int i = 0; i < line.length(); i++) {
                if(line.charAt(i) == ':') {
                    key = line.substring(0, i);
                    break;
                }
            }
            
            key = key.trim();
            int hash = (getPath() + key).hashCode();
            
            keyCounters.put(hash, keyCounters.getOrDefault(hash, -1) + 1);
            
            return key + ":" + keyCounters.get(hash);
        }
        
        private int getSpaceFromLine(String line) {
            int spaces = 0;
            for(;spaces < line.length(); spaces++)
                if(line.charAt(spaces) != ' ')
                    break;
            
            if (line.trim().startsWith("- ")) { // array list special case
                spaces -= 1;
            }
            
            return spaces;
        }
        
        private int getLayerFromLine(Stack<Integer> currentLevel, int spaces) {           
            int i = 0;
            for(; i < currentLevel.size(); i++)
                if(currentLevel.get(i) == spaces)
                    break;
            return i;
        }
    }
}