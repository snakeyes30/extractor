package org.sam.extractor.Configuration;

import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * Created by RShastri on 5/5/2017.
 */
public class ApplcationConfigurationTest {


    @Test
    public void name() throws Exception {
        List<String> strings = Arrays.asList("C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E01.The.Good.Son.DVDRip.XviD-VF\\frasier.s01e01.dvdrip.xvid-vf.rar",
                "C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E02.Space.Quest.DVDRip.XviD-VF\\frasier.s01e02.dvdrip.xvid-vf.rar", " C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E03.Dinner.At.Eight.DVDRip.XviD-VF\\frasier.s01e03.dvdrip.xvid-vf.rar", " C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\Frasier.S01.DVDRip.XviD-SCC\\Frasier.S01E05.Heres.Looking.At.You.DVDRip.XviD-VF\\frasier.s01e05.dvdrip.xvid-vf.rar", " C:\\Users\\rshastri\\Desktop\\Frasier.S01-S11.DVDRip.XviD-SCC\\frasier.s01e05.dvdrip.xvid-vf.rar");

        Map<String, List> map = new HashMap<>();
        strings.forEach(s -> {
                    String[] split = s.split(File.pathSeparator);
                    for (int i = 0; i < split.length; i++) {
                        String sp = split[i];
                        String next = null;
                        if (i + 1 < split.length) {
                            next = split[i + 1];
                        }
                        List list = getList(map, i, split);

                    }
                }
        );

    }

    private List getList(Map<String, List> map, int sp, String[] next) {
        if (next == null) {
            return new ArrayList();
        }
        return map.computeIfAbsent(sp, s1 -> new ArrayList());
    }
}