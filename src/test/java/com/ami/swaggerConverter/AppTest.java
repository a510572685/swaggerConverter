package com.ami.swaggerConverter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

import org.junit.Before;
import org.junit.Test;

public class AppTest {
  private Path resourcesPath = null;
  private String fileName = null;
  
  @Before
  public void setUp() throws IOException, URISyntaxException {
    resourcesPath = Paths.get(AppTest.class.getClassLoader().getResource("./").toURI());
    fileName = Files.list(resourcesPath).filter(file -> file.toString().endsWith(".yaml")).findFirst().get().getFileName().toString().replaceAll("\\.yaml$", "");
    Files.list(resourcesPath)
      .filter(Files::isRegularFile)
      .filter(file -> !(file.toString().endsWith(".yaml") || file.toString().endsWith(".css"))).forEach(file -> {
      try {
        Files.delete(file);
      } catch (IOException e) {
      }
    });
    
  }

  @Test
  public void testMainWithNoArguments() throws URISyntaxException {
    App.main(null);
    assertTrue(Files.exists(resourcesPath.resolve(fileName + ".html")));
    assertTrue(Files.exists(resourcesPath.resolve(fileName + ".pdf")));
  }
  
  @Test
  public void testMainWithDirArgument() throws URISyntaxException {
    String[] args = new String[] {resourcesPath.toString()};
    App.main(args);
    assertTrue(Files.exists(resourcesPath.resolve(fileName + ".html")));
    assertTrue(Files.exists(resourcesPath.resolve(fileName + ".pdf")));
  }

}
