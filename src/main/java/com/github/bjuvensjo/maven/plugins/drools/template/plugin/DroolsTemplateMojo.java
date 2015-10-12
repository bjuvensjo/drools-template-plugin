package com.github.bjuvensjo.maven.plugins.drools.template.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Generates drools template.
 *
 * @author Magnus Bjuvensjö
 * @goal generate
 * @phase generate-sources
 */
public class DroolsTemplateMojo extends AbstractMojo {
    private Generator generator;
    /**
     * Charset for generated request and response classes.
     *
     * @parameter default-value="UTF-8"
     * @required
     */
    private String charset;
    /**
     * Base directory.
     *
     * @parameter default-value="${project.basedir}"
     * @required
     */
    private File baseDirectory;
    /**
     * Input directory.
     *
     * @parameter default-value="${project.basedir}/src/main/resources"
     * @required
     */
    private File inputDirectory;
    /**
     * Output directory.
     *
     * @parameter default-value="${project.build.directory}/generated-sources/drools"
     * @required
     */
    private File outputDirectory;
    /**
     * Maven project.
     *
     * @parameter default-value="${project}"
     */
    private MavenProject mavenProject;

    /**
     * @throws org.apache.maven.plugin.MojoExecutionException if an unexpected problem occurs. Throwing this exception causes a "BUILD ERROR"
     *                                                        message to be displayed
     * @throws org.apache.maven.plugin.MojoFailureException   if an expected problem (such as a compilation failure) occurs. Throwing this
     *                                                        exception causes a "BUILD FAILURE" message to be displayed
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            final Log log = getLog();

            log.info("outputDirectory: " + outputDirectory.getCanonicalPath());

            generator = new Generator(baseDirectory, inputDirectory, outputDirectory, charset);

            List<File> drls = generator.generateDrls();
            log.info("Generated drls: " + drls);

            mavenProject.addCompileSourceRoot(outputDirectory.getPath());
            log.info("Added " + outputDirectory.getCanonicalPath() + " as compile source root");
        } catch (IOException e) {
            throw new MojoFailureException("Can not generate", e);
        }
    }
}
