package com.otilm.api.testsupport;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

/**
 * Finds the unions the model package publishes, so a document-wide invariant does not have to be kept as a hand-written
 * list of families.
 */
public final class PublishedUnions {

    private static final String MODEL_PACKAGE = "com.otilm.api.model";
    private static final String CLASS_SUFFIX = ".class";

    private PublishedUnions() {
    }

    /**
     * Every type carrying a {@code @Schema(oneOf = ...)}, which is the only way this module declares a union. Nested
     * schema-only views count, since that is how some families keep the union off their Jackson-annotated type.
     */
    public static List<Class<?>> declaringClasses() {
        return modelPackageRoots()
                .stream()
                .flatMap(PublishedUnions::classNamesUnder)
                .distinct()
                .sorted()
                .map(PublishedUnions::load)
                .filter(PublishedUnions::declaresAOneOf)
                .toList();
    }

    private static Stream<String> classNamesUnder(Path root) {
        try (Stream<Path> files = Files.walk(root)) {
            return files
                    .filter(path -> path.toString().endsWith(CLASS_SUFFIX))
                    .map(path -> className(root, path))
                    .toList()
                    .stream();
        } catch (IOException e) {
            throw new UncheckedIOException("cannot walk " + root, e);
        }
    }

    private static boolean declaresAOneOf(Class<?> type) {
        Schema schema = type.getAnnotation(Schema.class);
        return schema != null && schema.oneOf().length > 0;
    }

    /**
     * Every classpath entry holding the package, not just the first: the test classes share it, and their directory
     * comes first.
     */
    private static List<Path> modelPackageRoots() {
        List<Path> roots = new ArrayList<>();
        try {
            Enumeration<URL> located = PublishedUnions.class
                    .getClassLoader()
                    .getResources(MODEL_PACKAGE.replace('.', '/'));
            while (located.hasMoreElements()) {
                roots.add(directory(located.nextElement()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("cannot locate " + MODEL_PACKAGE, e);
        }
        if (roots.isEmpty()) {
            throw new IllegalStateException(MODEL_PACKAGE + " is not on the test classpath");
        }
        return roots;
    }

    private static Path directory(URL located) {
        try {
            return Path.of(located.toURI());
        } catch (URISyntaxException | RuntimeException e) {
            throw new IllegalStateException("the model package must be readable as a directory, was " + located, e);
        }
    }

    private static String className(Path root, Path classFile) {
        String relative = root.relativize(classFile).toString();
        return MODEL_PACKAGE + '.'
                + relative.substring(0, relative.length() - CLASS_SUFFIX.length()).replace(File.separatorChar, '.');
    }

    /**
     * Loaded without initialization: reading an annotation needs no static state, and a class initializer that fails
     * would say nothing about the contract.
     */
    private static Class<?> load(String className) {
        try {
            return Class.forName(className, false, PublishedUnions.class.getClassLoader());
        } catch (ClassNotFoundException | LinkageError e) {
            throw new IllegalStateException("compiled but not loadable: " + className, e);
        }
    }
}
