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
            htmlElementDTO.addAttribute("type", "submit");
            htmlElementDTO.addAttribute("class", "bg-black");
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
            String process = ReflectionUtils.getFieldValue(clickProcess.get(i), "getName");
            if ("com.ibm.bsch.client.launcher.ExecuteTransaction".equals(process)) {
                createServiceCallFunction(panelDTO, clickProcess.get(i), clickBody);
            } else if ("com.ibm.bsch.client.launcher.LauncherCrossRelationByName".equals(process)) {
                createRelationCallFunction(clickProcess.get(i), clickBody);
            } else {
                String data = ReflectionUtils.getFieldValue(clickProcess.get(i), "getData");
                if(!StringUtils.isEmpty(data)) {
                    panelDTO.getComponent().checkDeclaration(data.split(",")[0], "null");
                }

                logNotImplementedAction(clickProcess.get(i), clickBody);
            }
        }
    }

    private void logNotImplementedAction(Object process, StringBuilder clickBody) {

        String name = ReflectionUtils.getFieldValue(process, "getName");
        String processData = ReflectionUtils.getFieldValue(process, "getData");
        String processParameters = ReflectionUtils.getFieldValue(process, "getParameters");
        String processOutData = ReflectionUtils.getFieldValue(process, "getOutData");

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\"Not implemented action: %s\\n", name));

        if (StringUtils.isEmpty(processData)) {
            builder.append(String.format("clickProcessData: %s\\n", processData));
        }

        if (StringUtils.isEmpty(processParameters)) {
            builder.append(String.format("clickProcessParameters: %s\\n", processData));
        }

        if (StringUtils.isEmpty(processOutData)) {
            builder.append(String.format("clickProcessOutData: %s\\n", processOutData));
        }

        builder.append("\"");
        clickBody.append(TypeScriptTemplateUtils.getFunctionCall(null, "console.warn", false, false, builder.toString()));
    }

    public String getClickProcessFunctionName(String componentName) {
        if (StringUtils.isEmpty(componentName)) {
            return null;
        }
        return "click" + componentName.replaceAll(Constants.REGEX_REMOVE_SPECIAL_CHARACTERS, "");

    }


    private void createRelationCallFunction(Object process, StringBuilder body) {
        String data = ReflectionUtils.getFieldValue(process, "getData");
        String[] relations = data.split(",");

        for (String relation : relations) {
            body.append(TypeScriptTemplateUtils.getFunctionCall(null, BSCHCrossRelationComponentResolver.getFunctionName(relation), false, true));
        }
    }

    public void createServiceCallFunction(PanelDTO panelDTO, Object process, StringBuilder body) {

        String data = ReflectionUtils.getFieldValue(process, "getData");
        String outData = ReflectionUtils.getFieldValue(process, "getOutData");

        String[] processData = data.split(",");


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

        String returnVariable = StringUtils.isEmpty(outData) ? null : getReturnVariable(outData);

        if (!StringUtils.isEmpty(returnVariable)) {
            panelDTO.getComponent().checkDeclaration(returnVariable, "null");
        }

        body.append(TypeScriptTemplateUtils.getFunctionCall(returnVariable,
                Character.toLowerCase(Constants.GLOBAL_SERVICE_NAME.charAt(0)) + Constants.GLOBAL_SERVICE_NAME.substring(1) + "." + ServiceWriter.getServiceFunctionName(processData[0]),
                !serviceDescription.isPresent(), true, params));

    }

    private String getParamVariable(String expression) {

        if (!StringUtils.isEmpty(expression) && expression.contains("*")) {
            String[] split = expression.split("\\*");

            if(split[1].startsWith("'") || split[1].startsWith("\"")){
                return split[1];
            }

            return "this." + split[1];
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
