package com.example.tfc.factory.service;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.ServiceDescriptionDTO;
import com.example.tfc.factory.resolver.ResolverManager;
import com.example.tfc.factory.utils.ReflectionUtils;
import com.example.tfc.factory.writer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

@Service
public class LoadService {

    private HTMLWriter htmlWriter;
    private ProjectWriter projectWriter;
    private ComponentWriter componentWriter;
    private ServiceWriter serviceWriter;

    @Autowired
    public LoadService(@Qualifier("htmlWriter") HTMLWriter htmlWriter, @Qualifier("projectWriter") ProjectWriter projectWriter, @Qualifier("componentWriter") ComponentWriter componentWriter, @Qualifier("serviceWriter") ServiceWriter serviceWriter) {
        this.htmlWriter = htmlWriter;
        this.projectWriter = projectWriter;
        this.componentWriter = componentWriter;
        this.serviceWriter = serviceWriter;
    }

    public void load(String clazz) {

        URL[] urls = {};

        try (JarFileLoader loader = new JarFileLoader(urls)) {

            loader.addFile(Constants.EXTERNAL_JARS_FOLDER_PATH + "test.jar");
            Class<?> aClass = loader.loadClass(clazz);

            Object instance = aClass.newInstance();
            Method exec = aClass.getMethod("exec");

            Object panel = exec.invoke(instance);
            buildPanel(panel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildPanel(Object panel) throws InvocationTargetException, IllegalAccessException {

        List<Method> getters = ReflectionUtils.getAllNonNullFieldGetters(panel);

        Method componentsMethod = getters.stream().filter(m -> "getComponents".equals(m.getName())).findFirst().orElse(null);

        PanelDTO panelDTO = PanelDTO.init();

        loadServices(panelDTO);
        ResolverManager.getResolver(panel.getClass().getName()).resolve(panel, panelDTO);

        getters.remove(componentsMethod);
        buildComponents((List) componentsMethod.invoke(panel), 0, panelDTO);

        projectWriter.write(panelDTO);
        htmlWriter.write(panelDTO);
        componentWriter.write(panelDTO);
        serviceWriter.write(panelDTO);
        projectWriter.bindFiles(panelDTO);
    }

    private void buildComponents(List components, int i, PanelDTO panelDTO) {

        if (CollectionUtils.isEmpty(components) || i == components.size()) {
            return;
        }

        PanelDTO resolve = ResolverManager.getResolver(components.get(i).getClass().getName()).resolve(components.get(i), panelDTO);
        buildComponents(components, ++i, resolve);
    }

    public void loadServices(PanelDTO panelDTO) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("services.csv").getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String[] args = scanner.nextLine().split(",");

                panelDTO.getService().getDescriptions().add(new ServiceDescriptionDTO(args[0], args[1], args[2]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
