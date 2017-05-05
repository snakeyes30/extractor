package org.sam.extractor.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.util.*;

/**
 * Created by RShastri on 5/5/2017.
 */
public class ApplcationConfigurationTest {


    private ObjectMapper objectMapper;

    @Test
    public void name() throws Exception {
        List<String> strings = Arrays.asList("C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E01.The.Good.Son.DVDRip.XviD-VF\\frasier.s01e01.dvdrip.xvid-vf.rar",
                "C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E02.Space.Quest.DVDRip.XviD-VF\\frasier.s01e02.dvdrip.xvid-vf.rar", "C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E03.Dinner.At.Eight.DVDRip.XviD-VF\\frasier.s01e03.dvdrip.xvid-vf.rar", "C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E05.Heres.Looking.At.You.DVDRip.XviD-VF\\frasier.s01e05.dvdrip.xvid-vf.rar", "C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\frasier.s01e05.dvdrip.xvid-vf.rar");


//        List<String> strings = Arrays.asList("C:\\Users\\", "C:\\Users\\Cheese", "C:\\Users\\Cake");
        Map<String, List> map = new HashMap<>();
        strings.forEach(s -> {
                    String[] split = s.split("\\\\");
//                    for (int i = 0; i < split.length; i++) {
//                        String sp = split[i];
                        try {
                            getList(map, 0, split);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

//                    }
                }
        );
        System.out.println(map);
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println(objectMapper.writeValueAsString(map));

    }

    /**
     * {
     * "C": [
     * `        "Users": [
     * <p>
     * ]
     * ]
     * }
     *
     * @param map
     * @param sp
     * @param next
     * @return
     */
    private Map getList(Map map, int sp, String[] next) throws JsonProcessingException {
        if (sp >= next.length) {
            return new HashMap();
        }
        String nextSp = next[sp];
        System.out.println("Working on file part " + nextSp);
        Map newMap = new HashMap();
        if (map.containsKey(nextSp)) {
            newMap = (Map) map.get(nextSp);
        }
        Map list = getList(newMap, sp + 1, next);

            map.put(nextSp, list);

//        System.out.println(next[sp]);

        return map;

    }
}