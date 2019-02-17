package com.example.tfc.factory.resolver.component;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.HTMLElementDTO;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.ServiceDescriptionDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFunctionDTO;
import com.example.tfc.factory.commons.enums.HTMLElementType;
import com.example.tfc.factory.utils.ReflectionUtils;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import com.example.tfc.factory.writer.ServiceWriter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BSCHButtonComponentResolver extends ComponentResolver {

    @Override
    public PanelDTO resolve(Object component, PanelDTO panelDTO) {

        HTMLElementDTO htmlElementDTO = new HTMLElementDTO();
        htmlElementDTO.setType(HTMLElementType.BUTTON);
        htmlElementDTO.setText(ReflectionUtils.getFieldValue(component, "getText"));
        String name = ReflectionUtils.getFieldValue(component, "getName");
        String clickFunctionName = getClickProcessFunctionName(name);

        if (!StringUtils.isEmpty(name)) {
            htmlElementDTO.addAttribute("(click)", clickFunctionName + "()");
        }

        panelDTO.getHtml().getElements().add(
                htmlElementDTO
                        .addAttribute("name", name)
        );

        buildClickFunction(component, panelDTO, clickFunctionName);

        return panelDTO;
    }

    private void buildClickFunction(Object component, PanelDTO panelDTO, String clickFunctionName) {
        StringBuilder clickBody = new StringBuilder();

        getFunctionCalls(panelDTO, component, clickBody);

        panelDTO.getComponent().getFunctions().add(new TypeScriptFunctionDTO("", clickFunctionName, null, clickBody.toString(), null));
    }

    private void getFunctionCalls(PanelDTO panelDTO, Object component, StringBuilder clickBody) {
        List clickProcess = ReflectionUtils.getListField(component, "getClickProcess");
        for (int i = 0; i < clickProcess.size(); i++) {
            if ("com.ibm.bsch.client.launcher.ExecuteTransaction".equals(clickProcess.get(i))) {
                createServiceCallFunction(panelDTO, component, i, clickBody);
            } else if ("com.ibm.bsch.client.launcher.LauncherCrossRelationByName".equals(clickProcess.get(i))) {
                createRelationCallFunction(component, i, clickBody);
            }
        }
    }

    public String getClickProcessFunctionName(String componentName) {
        if (StringUtils.isEmpty(componentName)) {
            return null;
        }
        return "click" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");

    }


    private void createRelationCallFunction(Object component, int index, StringBuilder body) {
        List clickProcessData = ReflectionUtils.getListField(component, "getClickProcessData");
        String[] relations = clickProcessData.get(index).toString().split(",");

        for (String relation : relations) {
            body.append(TypeScriptTemplateUtils.getFunctionCall(null, BSCHCrossRelationComponentResolver.getFunctionName(relation), false));
        }
    }

    public void createServiceCallFunction(PanelDTO panelDTO, Object component, int index, StringBuilder body) {
        List clickProcessData = ReflectionUtils.getListField(component, "getClickProcessData");
        List clickProcessOutData = ReflectionUtils.getListField(component, "getClickProcessOutData");
        String[] processData = clickProcessData.get(index).toString().split(",");


        Optional<ServiceDescriptionDTO> serviceDescription = panelDTO.getService().getDescriptions().stream().filter(d -> d.getName().equals(processData[0])).findFirst();

        String[] params;

        if (serviceDescription.isPresent() && "get".equals(serviceDescription.get().getMethod()) && processData.length > 1) {
            StringBuilder query = new StringBuilder();
            query.append("`?");
            for (int i = 1; i < processData.length; i++) {
                if (processData[i].contains("*")) {
                    String[] split = processData[i].split("\\*");
                    query.append(split[0]);
                    query.append("=${");
                    query.append(getParamVariable(processData[i]));
                    query.append("}");

                    if (i + 1 < processData.length) {
                        query.append("&");
                    }
                }
            }
            query.append("`");
            params = new String[]{query.toString()};
        } else {
            ArrayList<String> paramList = new ArrayList<>();
            for (String processDatum : processData) {
                paramList.add(getParamVariable(processDatum));
            }
            params = paramList.toArray(new String[paramList.size()]);
        }

        String returnVariable = clickProcessOutData.size() <= index ? null : getReturnVariable(clickProcessOutData.get(index).toString());

        body.append(TypeScriptTemplateUtils.getFunctionCall(returnVariable,
                "this." + Character.toLowerCase(Constants.GLOBAL_SERVICE_NAME.charAt(0)) + Constants.GLOBAL_SERVICE_NAME.substring(1) + "." + ServiceWriter.getServiceFunctionName(processData[0]),
                !serviceDescription.isPresent(), params));

    }

    private String getParamVariable(String expression) {

        if (!StringUtils.isEmpty(expression) && expression.contains("*")) {
            return "this." + expression.split("\\*")[1];
        }

        return "''";
    }

    private String getReturnVariable(String expression) {
        if (!StringUtils.isEmpty(expression) && expression.contains("*")) {
            return expression.split("\\*")[0];
        }
        return "";
    }
}
