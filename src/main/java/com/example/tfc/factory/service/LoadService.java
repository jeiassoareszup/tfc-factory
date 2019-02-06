package com.example.tfc.factory.service;

import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.resolver.component.ComponentResolverManager;
import com.example.tfc.factory.utils.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

@Service
public class LoadService {

    private static final String EXTERNAL_JARS_FOLDER_PATH = "/Users/jeiassoares/class/";

    public void load() {

        URL[] urls = {};

        try (JarFileLoader loader = new JarFileLoader(urls)) {

            loader.addFile(EXTERNAL_JARS_FOLDER_PATH + "test.jar");
            Class<?> aClass = loader.loadClass("com.ibm.bsch.client.bmlclasses.BRVR085");

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

        PanelDTO panelDTO = ComponentResolverManager.getResolver(panel.getClass().getName()).resolve(panel, PanelDTO.init());

        getters.remove(componentsMethod);
        buildComponents((List) componentsMethod.invoke(panel), 0, panelDTO);

        System.out.println(panelDTO);
    }

    private void buildComponents(List components, int i, PanelDTO panelDTO) {

        if (CollectionUtils.isEmpty(components) || i == components.size()) {
            return;
        }

        PanelDTO resolve = ComponentResolverManager.getResolver(components.get(i).getClass().getName()).resolve(components.get(i), panelDTO);
        buildComponents(components, ++i, resolve);
    }

}
