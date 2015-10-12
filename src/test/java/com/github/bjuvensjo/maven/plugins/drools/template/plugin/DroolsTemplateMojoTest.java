package com.github.bjuvensjo.maven.plugins.drools.template.plugin;


import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class DroolsTemplateMojoTest {

    @Test
    public void testExecutePerson() throws Exception {
        DroolsTemplateMojo droolsTemplateMojo = new DroolsTemplateMojo();

        Class<?> droolsTemplateMojoClass = droolsTemplateMojo.getClass();

        Field inputDirectory = droolsTemplateMojoClass.getDeclaredField("inputDirectory");
        inputDirectory.setAccessible(true);
        inputDirectory.set(droolsTemplateMojo, new File("src/test/resources"));

        File outputDir = new File("target/generated-sources/drools");
        Field outputDirectory = droolsTemplateMojoClass.getDeclaredField("outputDirectory");
        outputDirectory.setAccessible(true);
        outputDirectory.set(droolsTemplateMojo, outputDir);

        Field baseDirectory = droolsTemplateMojoClass.getDeclaredField("baseDirectory");
        baseDirectory.setAccessible(true);
        baseDirectory.set(droolsTemplateMojo, new File("."));

        Field charset = droolsTemplateMojoClass.getDeclaredField("charset");
        charset.setAccessible(true);
        charset.set(droolsTemplateMojo, "UTF-8");

        Field mavenProject = droolsTemplateMojoClass.getDeclaredField("mavenProject");
        mavenProject.setAccessible(true);
        MavenProject project = mock(MavenProject.class);
        mavenProject.set(droolsTemplateMojo, project);

        droolsTemplateMojo.execute();

        File personGeneratedFile = new File(outputDir, "org/example/PersonGenerated.drl");
        assertTrue(personGeneratedFile.exists());

//        FileUtils.deleteQuietly(personGeneratedFile);
    }
}
