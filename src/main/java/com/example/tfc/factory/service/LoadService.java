package com.example.tfc.factory.service;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.resolver.ResolverManager;
import com.example.tfc.factory.utils.ReflectionUtils;
import com.example.tfc.factory.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

@Service
public class LoadService {

    private Writer htmlWriter;
    private Writer projectWriter;
    private Writer componentWriter;

    @Autowired
    public LoadService(@Qualifier("htmlWriter") Writer htmlWriter, @Qualifier("projectWriter") Writer projectWriter, @Qualifier("componentWriter") Writer componentWriter) {
        this.htmlWriter = htmlWriter;
        this.projectWriter = projectWriter;
        this.componentWriter = componentWriter;
    }

    public void load() {

        URL[] urls = {};

        try (JarFileLoader loader = new JarFileLoader(urls)) {

            loader.addFile(Constants.EXTERNAL_JARS_FOLDER_PATH + "test.jar");
            Class<?> aClass = loader.loadClass("com.ibm.bsch.client.bmlclasses.BRVR086");

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

        PanelDTO panelDTO = ResolverManager.getResolver(panel.getClass().getName()).resolve(panel, PanelDTO.init());

        getters.remove(componentsMethod);
        buildComponents((List) componentsMethod.invoke(panel), 0, panelDTO);

        projectWriter.write(panelDTO);
        htmlWriter.write(panelDTO);
        componentWriter.write(panelDTO);
    }

    private void buildComponents(List components, int i, PanelDTO panelDTO) {

        if (CollectionUtils.isEmpty(components) || i == components.size()) {
            return;
        }

        PanelDTO resolve = ResolverManager.getResolver(components.get(i).getClass().getName()).resolve(components.get(i), panelDTO);
        buildComponents(components, ++i, resolve);
    }

}
