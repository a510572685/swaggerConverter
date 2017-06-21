package com.ami.swaggerConverter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.asciidoctor.*;
import org.asciidoctor.Asciidoctor.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.swagger2markup.Swagger2MarkupConverter;

public class App {
  final static Logger log = LoggerFactory.getLogger(App.class);

  public static void main(final String[] args) {
    try {
      final Path workDirectory;
      if(args == null || args.length == 0) {
        workDirectory = Paths.get(App.class.getClassLoader().getResource("./").toURI());
      } else {
        workDirectory = Paths.get(args[0]);
      }
      
      // treat every yaml file in the work directory as source file
      final List<Path> sourceFiles = Files.list(workDirectory).filter(file -> file.toString().endsWith(".yaml")).collect(Collectors.toList());
      
      sourceFiles.forEach(sourceFile -> {
        final Path adocFile = sourceFile.resolveSibling(sourceFile.getFileName().toString().concat("-adoc"));      
        Swagger2MarkupConverter.from(sourceFile).build().toFileWithoutExtension(adocFile);
        generateHtml(adocFile, workDirectory);
        generatePdf(adocFile);
      });
      
      log.info("Done!");
    } catch (Throwable t) {
      log.error("something wrong happened.");
    }
    
  }
  
  private static void generateHtml(final Path adocFile, final Path workDirectory) {
    try {
      // search css file
      Optional<Path> styleFile = Files.list(workDirectory).filter(file -> file.getFileName().toString().endsWith(".css")).findFirst();
      Asciidoctor asciidoctor = Factory.create();
      Attributes attributes = AttributesBuilder.attributes().get();
      Options options = OptionsBuilder.options().attributes(attributes).get();
      asciidoctor.convertFile(adocFile.toFile(), options);
      final Path htmlFile = adocFile.resolveSibling(adocFile.getFileName().toString().replaceAll("\\.yaml-adoc$", ".html"));
      styleFile.orElseGet(() -> {
        try {
          String styleContent = IOUtils.toString(App.class.getClassLoader().getResourceAsStream("style.css"), StandardCharsets.UTF_8);
          List<String> lines = Files.readAllLines(htmlFile, StandardCharsets.UTF_8);
          lines.add(4, "<style>" + System.lineSeparator());
          lines.add(5, styleContent + System.lineSeparator() + "</style>");
          Files.write(htmlFile, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
          log.error(e.getMessage());
        }
        return Paths.get("");
      });
      styleFile.ifPresent(style -> {
        try {
          List<String> lines = Files.readAllLines(htmlFile, StandardCharsets.UTF_8);
          lines.add(4, "<style>" + System.lineSeparator());
          lines.add(5, Files.lines(style).collect(Collectors.joining()) + System.lineSeparator() + "</style>");
          Files.write(htmlFile, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
          e.printStackTrace();
        }        
      });
      log.info("html: " + htmlFile);
    } catch(IOException e) {
      // do nothing
      log.error(e.getMessage());
    }
  }
  
  private static void generatePdf(final Path adocFile) {
    final Asciidoctor asciidoctor = Factory.create();
    final Options options = OptionsBuilder.options().backend("pdf").get();
    asciidoctor.convertFile(adocFile.toFile(), options);
    final Path pdfFile = adocFile.resolveSibling(adocFile.getFileName().toString().replaceAll("\\.yaml-adoc$", ".pdf"));
    log.info("pdf: " + pdfFile);
  }

}
