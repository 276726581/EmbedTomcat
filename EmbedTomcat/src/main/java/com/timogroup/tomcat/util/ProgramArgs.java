package com.timogroup.tomcat.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by TimoRD on 2016/7/16.
 */
public final class ProgramArgs {

    private Map<String, List<String>> map;

    private ProgramArgs(Map<String, List<String>> map) {
        this.map = map;
    }

    public static ProgramArgs parse(String[] args) {
        Map<String, List<String>> map = new ConcurrentHashMap<>();
        ProgramArgs programArgs = new ProgramArgs(map);

        String arg;
        String key = null;
        for (int i = 0; i < args.length; i++) {
            arg = args[i];
            if (arg.startsWith("--")) {
                key = arg;
                List<String> list = new ArrayList<>();
                map.put(arg, list);
            } else if (null != key) {
                List<String> list = map.get(key);
                list.add(arg);
            } else {
                break;
            }
        }

        return programArgs;
    }

    public boolean hasKey(String key) {
        boolean result = map.containsKey(key);
        return result;
    }

    public int size() {
        int size = map.keySet().size();
        return size;
    }

    public String getArg(String key) {
        List<String> list = getArgs(key);
        if (null == list || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public List<String> getArgs(String key) {
        List<String> list = map.get(key);
        return list;
    }
}
