/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.axis.maven.java2wsdl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.axis.maven.shared.nsmap.Mapping;
import org.apache.axis.maven.shared.nsmap.MappingUtil;
import org.apache.axis.utils.ClassUtils;
import org.apache.axis.wsdl.fromJava.Emitter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.github.veithen.ulog.PlexusLoggerInjector;

/**
 * 
 * 
 * @goal generate-wsdl
 * @phase process-classes
 * @requiresDependencyResolution compile
 */
public class GenerateWsdlMojo extends AbstractMojo {
    /**
     * @component
     */
    // This is necessary to set up logging such that all messages logged by the Axis
    // libraries through commons logging are redirected to Plexus logs.
    PlexusLoggerInjector loggerInjector;
    
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The name of the class to generate a WSDL for. The class must be on the classpath.
     * 
     * @parameter
     * @required
     */
    private String className;

    /**
     * The target namespace for the interface.
     * 
     * @parameter
     * @required
     */
    private String namespace;
    
    /**
     * Mappings of packages to namespaces.
     * 
     * @parameter
     */
    private Mapping[] mappings;
    
    /**
     * The url of the location of the service. The name after the last slash or
     * backslash is the name of the service port (unless overridden by the -s
     * option). The service port address location attribute is assigned the
     * specified value.
     * 
     * @parameter
     * @required
     */
    private String location;
    
    /**
     * The name of the output WSDL file.
     * 
     * @parameter
     * @required
     */
    private File output;
    
    public void execute() throws MojoExecutionException, MojoFailureException {
        List classpath;
        try {
            classpath = project.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException ex) {
            throw new MojoExecutionException("Unexpected exception", ex);
        }
        URL[] urls = new URL[classpath.size()];
        for (int i=0; i<classpath.size(); i++) {
            try {
                urls[i] = new File((String)classpath.get(i)).toURL();
            } catch (MalformedURLException ex) {
                throw new MojoExecutionException("Unexpected exception", ex);
            }
        }
        // TODO: this will likely make the plugin non thread safe
        ClassUtils.setDefaultClassLoader(new URLClassLoader(urls));
        try {
            Emitter emitter = new Emitter();
            if (mappings != null && mappings.length > 0) {
                emitter.setNamespaceMap(MappingUtil.getPackageToNamespaceMap(mappings));
            }
            try {
                emitter.setCls(className);
            } catch (ClassNotFoundException ex) {
                throw new MojoFailureException("Class " + className + " not found");
            }
            emitter.setIntfNamespace(namespace);
            emitter.setLocationUrl(location);
            output.getParentFile().mkdirs();
            try {
                emitter.emit(output.getAbsolutePath(), Emitter.MODE_ALL);
            } catch (Exception ex) {
                throw new MojoFailureException("java2wsdl failed", ex);
            }
        } finally {
            // TODO: apparently this is a no-op
            ClassUtils.setDefaultClassLoader(null);
        }
    }
}