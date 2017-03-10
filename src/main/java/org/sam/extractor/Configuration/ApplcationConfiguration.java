package org.sam.extractor.Configuration;

import com.github.junrar.extract.ExtractArchive;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by RShastri on 3/6/2017.
 */

@RestController
@SpringBootApplication
public class ApplcationConfiguration {


    @RequestMapping("/xxx")
    String home() {
        return "Hello World!";
    }


    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<String> files() throws IOException {
        List<String> strings = Arrays.asList("L:\\","G:\\", "H:\\");

        try (Stream<Path> stream = strings.stream().parallel().flatMap(this::getPathStream)) {
            List<String> collect = stream.map(Path::toString).collect(Collectors.toList());
            return collect;
        }

    }


    private Stream<Path> getPathStream(String first) {
        try {
            Stream<Path> s = StreamSupport.stream(Files.newDirectoryStream(Paths.get(first)).spliterator(), false).parallel().flatMap(new FN<>());
            return s.filter(getPathBasicFileAttributesBiPredicate().and(xxgetPathBasicFileAttributesBiPredicate()));
        } catch (Exception e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }


    @RequestMapping("/extract")
    public String extract(@RequestParam("file") String file) {


        final File rar = new File(file);

        final File destinationFolder = new File(rar.getParent());
        ExtractArchive extractArchive = new ExtractArchive();
        extractArchive.extractArchive(rar, destinationFolder);
        return "done";
    }

    private Predicate<Path> getPathBasicFileAttributesBiPredicate() {
        Predicate<Path> pathBasicFileAttributesBiPredicate = (path) -> String.valueOf(path).endsWith(".rar");
        Predicate<Path> and = pathBasicFileAttributesBiPredicate.and((path) -> Files.isReadable(path));
        Predicate<Path> pathBasicFileAttributesBiPredicate1 = (path) -> {
            try {
                return !Files.isHidden(path);
            } catch (Exception e) {
                return false;
            }
        };
        return pathBasicFileAttributesBiPredicate1.and(and);
    }

    private Predicate<Path> xxgetPathBasicFileAttributesBiPredicate() {
        return (path) -> {
            try {
                return Files.find(path.getParent(), 1, (path1, basicFileAttributes) -> Pattern.matches(".*\\.(avi|mkv)", path1.toString())).count() == 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        };
    }

    static <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplcationConfiguration.class, args);
    }


    static class FN<T extends Path> implements Function<T, Stream<T>> {
        @Override
        public Stream<T> apply(T p) {
            if (!Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS)) {
                return Stream.of(p);
            } else {
                try {
                    return toStream(Files.newDirectoryStream(p)).flatMap(q -> apply((T) q));
                } catch (IOException ex) {
                    return Stream.empty();
                }
            }
        }

    }
}
