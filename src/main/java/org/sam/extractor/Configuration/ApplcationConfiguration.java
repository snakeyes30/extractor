package org.sam.extractor.Configuration;

import com.github.junrar.extract.ExtractArchive;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptyList;

/**
 * Created by RShastri on 3/6/2017.
 */

@RestController
@SpringBootApplication
//@ConfigurationProperties
public class ApplcationConfiguration {

    @Value("#{'${paths}'.split(',')}")
    private List<String> rootPaths;

    static <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplcationConfiguration.class, args);
    }

    @RequestMapping("/xxx")
    String home() {
        return "Hello World!";
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<String> files() throws IOException {

        try (Stream<Path> stream = rootPaths.stream().parallel().flatMap(this::getPathStream)) {
            List<String> collect = stream.map(Path::toString).collect(Collectors.toList());
            return collect;
        }

    }

    @RequestMapping(value = "/files2", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    Node files2() throws IOException {

        try (Stream<Path> stream = rootPaths.stream().parallel().flatMap(this::getPathStream)) {
            List<String> collect = stream.map(Path::toString).collect(Collectors.toList());
            Node node = new Node("Root");
            collect.forEach(s -> {
                        String[] split = s.split("\\\\");
                        getNode(node, 0, split);

                    }
            );
            return node;
        }

    }


    private Node getNode(Node root, int i, String[] paths) {

        if (i >= paths.length) {

            return new Node("", emptyList(), "");
        }

        String current = paths[i];
        System.out.println("Working on file part " + current);
        Optional<Node> node = root.getChildren().stream().filter(n -> n.getName().equals(current)).findAny();
        if (node.isPresent()) {
            getNode(node.get(), i + 1, paths);
        } else {
            String action = "";
            if (current.endsWith("rar")) {
                action = String.join("\\", paths);
            }
            Node x = new Node(current, emptyList(), action);
            root.getChildren().add(x);
            Node newNode = getNode(x, i + 1, paths);
        }

        ;

        return root;
    }
//    private Consumer<String> getStringConsumer(Map<String, List> map) {
//        return s -> {
//            doStuff(map, s);
//        };
//    }
//
//    private void doStuff(Map<String, Map> map, String s) {
//        String[] split = s.split(File.pathSeparator);
//
//
//    }

    private void getMap(Map<String, Object> map, String s) {
        String[] split = s.split(File.pathSeparator);
        for (String node : split) {
            get(map, node);
        }
    }

    private Object get(Map<String, Object> map, String node) {
        return map.putIfAbsent(node, new HashMap<>());
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
